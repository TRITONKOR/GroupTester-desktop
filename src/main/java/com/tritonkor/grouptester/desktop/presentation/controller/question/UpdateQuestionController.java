package com.tritonkor.grouptester.desktop.presentation.controller.question;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.FileService;
import com.tritonkor.grouptester.desktop.domain.QuestionService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.request.question.SaveQuestionRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Answer;
import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateQuestionController {
    @Autowired
    private MainController mainController;
    @Autowired
    private FileService fileService;

    @FXML
    private TextField questionTextField;
    @FXML
    private ImageView photoImageView;

    @FXML
    private HBox answerBox1;
    @FXML
    private HBox answerBox2;
    @FXML
    private HBox answerBox3;
    @FXML
    private HBox answerBox4;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Question currentQuestion;
    private List<Answer> questionAnswers;


    @FXML
    public void initialize() {
        objectMapper.registerModule(new JavaTimeModule());
        currentQuestion = QuestionService.getCurrentQuestion();
        questionAnswers = currentQuestion.getAnswers();
        if(currentQuestion != null) {
            writeQuestionData(currentQuestion);
        }
    }

    @FXML
    private void handlePhotoImageClick() throws IOException {
        FileChooser fileChooser = new FileChooser();
        var extensionFilter = new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        Path path = fileChooser.showOpenDialog(null).toPath();

        if (!path.toString().isBlank()) {
            Image image = new Image(path.toUri().toString());
            photoImageView.setImage(image);
            currentQuestion.setImage(fileService.getBytes(path));
        }
    }
    public void writeQuestionData(Question question) {
        Platform.runLater(() -> {
            questionTextField.setText(question.getText());

            byte[] image = question.getImage();
            if (image != null) {
                InputStream inputStream = new ByteArrayInputStream(image);
                photoImageView.setImage(new Image(inputStream));
            }

            if (questionAnswers.size() >= 4) {
                writeAnswerData(questionAnswers.get(0), answerBox1);
                writeAnswerData(questionAnswers.get(1), answerBox2);
                writeAnswerData(questionAnswers.get(2), answerBox3);
                writeAnswerData(questionAnswers.get(3), answerBox4);
            }
        });
    }

    private void writeAnswerData(Answer answer, HBox answerBox) {
        TextField textField = null;
        RadioButton radioButton = null;

        for (Node node : answerBox.getChildren()) {
            if (node instanceof TextField) {
                textField = (TextField) node;
            } else if (node instanceof RadioButton) {
                radioButton = (RadioButton) node;
            }

            if (textField != null && radioButton != null) {
                textField.setText(answer.getText());
                radioButton.setSelected(answer.getIsCorrect());
            }
        }
    }

    @FXML
    private void handleSave() throws IOException {
        List<Answer> answers = new ArrayList<>();

        answers.add(extractAnswer(answerBox1, questionAnswers.get(0).getId()));
        answers.add(extractAnswer(answerBox2, questionAnswers.get(1).getId()));
        answers.add(extractAnswer(answerBox3, questionAnswers.get(2).getId()));
        answers.add(extractAnswer(answerBox4, questionAnswers.get(3).getId()));

        for (Answer answer : answers) {
            if (answer.getIsCorrect() == null) {
                throw new IllegalArgumentException("Поле 'isCorrect' не може бути порожнім");
            }
        }

        SaveQuestionRequest request = SaveQuestionRequest.builder()
                .id(QuestionService.getCurrentQuestion().getId())
                .userId(AuthorizeService.getCurrentUser().getId())
                .text(questionTextField.getText())
                .image(currentQuestion.getImage())
                .answers(answers)
                .testId(TestService.getCurrentTest().getId())
                .build();

        HttpResponse<String> response = TestService.saveQuestion(request);
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            TestService.setCurrentTest(
                    objectMapper.readValue(jsonResponse, new TypeReference<Test>() {
                    }));
        } else {
            throw new RuntimeException("Failed to fetch tests: " + response.statusCode());
        }
        String fxmlFile = "view/question/QuestionList.fxml";
        mainController.switchPage(fxmlFile);
    }

    private Answer extractAnswer(HBox answerBox, UUID answerId) {
        TextField textField = null;
        RadioButton radioButton = null;

        for (Node node : answerBox.getChildren()) {
            if (node instanceof TextField) {
                textField = (TextField) node;
            } else if (node instanceof RadioButton) {
                radioButton = (RadioButton) node;
            }
        }

        if (textField != null && radioButton != null) {
            String text = textField.getText();
            boolean isSelected = radioButton.isSelected();
            return Answer.builder()
                    .id(answerId)
                    .questionId(QuestionService.getCurrentQuestion().getId())
                    .question(null)
                    .text(text)
                    .isCorrect(isSelected)
                    .build();
        }

        return null;
    }
}
