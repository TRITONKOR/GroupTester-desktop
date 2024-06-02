package com.tritonkor.grouptester.desktop.presentation.controller.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.QuestionService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.request.question.DeleteQuestionRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionListController {
    @Autowired
    private MainController mainController;
    @Autowired
    private CreateQuestionController createQuestionController;

    @FXML
    private ScrollPane questionsScrollPane;
    @FXML
    private Button createQuestionButton;
    @FXML
    private VBox vboxContainer;

    @FXML
    public void initialize() {
        updateQuestionList();
    }

    public void updateQuestionList() {
        List<Question> questions = TestService.getCurrentTest().getQuestions();

        vboxContainer.getChildren().clear();
        vboxContainer.getChildren().addAll(questions.stream().map(this::createQuestionCard).toList());
    }

    private BorderPane createQuestionCard(Question question) {
        Label textLabel = new Label(question.getText());
        textLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 22");

        Button deleteButton = new Button("Видалити");
        Button updateButton = new Button("Редагувати");

        deleteButton.getStyleClass().add("miniButton");
        updateButton.getStyleClass().add("miniButton");

        deleteButton.setOnAction(event -> handleDeleteQuestion(question));
        updateButton.setOnAction(event -> handleUpdateQuestion(question));

        deleteButton.setStyle("-fx-background-color: #41424A");

        BorderPane testCard = new BorderPane();
        VBox buttonVbox = new VBox(updateButton, deleteButton);
        buttonVbox.setStyle("-fx-spacing: 10; -fx-alignment: center");

        testCard.setLeft(textLabel);
        testCard.setRight(buttonVbox);

        testCard.setPrefWidth(1000.0);

        testCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return testCard;
    }

    @FXML
    public void handleCreateQuestion() {
        String fxmlFile = "view/question/CreateQuestion.fxml";

        mainController.switchPage(fxmlFile);
    }

    public void handleDeleteQuestion(Question question) {
        DeleteQuestionRequest request = DeleteQuestionRequest.builder()
                .questionId(question.getId())
                .userId(AuthorizeService.getCurrentUser().getId())
                .build();

        TestService.deleteQuestion(request);
        TestService.getCurrentTest().getQuestions().remove(question);
        updateQuestionList();
    }

    public void handleUpdateQuestion(Question question) {

        QuestionService.setCurrentQuestion(question);

        String fxmlFile = "view/question/UpdateQuestion.fxml";
        mainController.switchPage(fxmlFile);
    }
}
