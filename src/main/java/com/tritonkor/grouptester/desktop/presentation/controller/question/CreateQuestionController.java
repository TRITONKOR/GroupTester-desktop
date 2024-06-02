package com.tritonkor.grouptester.desktop.presentation.controller.question;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.FileService;
import com.tritonkor.grouptester.desktop.domain.QuestionService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.request.question.SaveQuestionRequest;
import com.tritonkor.grouptester.desktop.net.response.AnswerResponse;
import com.tritonkor.grouptester.desktop.persistence.entity.Answer;
import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.viewmodel.QuestionViewModel;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateQuestionController {

    @Autowired
    private MainController mainController;
    @Autowired
    private FileService fileService;

    @FXML
    private TextField questionTextField;
    @FXML
    private ImageView photoImageView;
    @FXML
    private Button uploadButton;

    @FXML
    private HBox answerBox1;
    @FXML
    private HBox answerBox2;
    @FXML
    private HBox answerBox3;
    @FXML
    private HBox answerBox4;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private QuestionViewModel questionViewModel;

    @FXML
    public void initialize() {
        objectMapper.registerModule(new JavaTimeModule());

        questionViewModel = new QuestionViewModel(null, "", null,
                null);

        bindFieldsToViewModel();
    }

    @FXML
    private void handlePhotoImageClick() {
        FileChooser fileChooser = new FileChooser();
        var extensionFilter = new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        Path path = fileChooser.showOpenDialog(null).toPath();

        if (!path.toString().isBlank()) {
            Image image = new Image(path.toUri().toString());
            uploadButton.setVisible(false);
            photoImageView.setImage(image);
            questionViewModel.setImage(image);
            questionViewModel.setImagePath(path);
        }
    }

    private void bindFieldsToViewModel() {
        questionTextField.textProperty().bindBidirectional(questionViewModel.textProperty());
        photoImageView.imageProperty().bindBidirectional(questionViewModel.imageProperty());
    }


    @FXML
    private void handleSave() throws IOException {
        List<Answer> answers = new ArrayList<>();

        answers.add(extractAnswer(answerBox1));
        answers.add(extractAnswer(answerBox2));
        answers.add(extractAnswer(answerBox3));
        answers.add(extractAnswer(answerBox4));

        for (Answer answer : answers) {
            if (answer.getIsCorrect() == null) {
                throw new IllegalArgumentException("Поле 'isCorrect' не може бути порожнім");
            }
        }

        SaveQuestionRequest request = SaveQuestionRequest.builder()
                .id(null)
                .userId(AuthorizeService.getCurrentUser().getId())
                .text(questionViewModel.getText())
                .image(questionViewModel.getImagePath() != null ? fileService.getBytes(questionViewModel.getImagePath()) : null)
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

    private Answer extractAnswer(HBox answerBox) {
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
                    .id(null)
                    .questionId(null)
                    .question(null)
                    .text(text)
                    .isCorrect(isSelected)
                    .build();
        }

        return null;
    }
}