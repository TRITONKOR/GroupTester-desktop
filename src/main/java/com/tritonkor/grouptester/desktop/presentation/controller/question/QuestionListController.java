package com.tritonkor.grouptester.desktop.presentation.controller.question;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @FXML
    private ScrollPane questionsScrollPane;
    @FXML
    private Button createQuestionButton;
    @FXML
    private VBox vboxContainer;

    private Test currentTest;

    @FXML
    public void initialize() throws JsonProcessingException {
        updateTestList();
    }

    public void updateTestList() throws JsonProcessingException {
        List<Question> questions = currentTest.getQuestions();

        vboxContainer.getChildren().clear();
        vboxContainer.getChildren().addAll(questions.stream().map(this::createQuestionCard).toList());
    }

    private BorderPane createQuestionCard(Question question) {
        Label textLabel = new Label(question.getText());
        textLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20");

        Button deleteButton = new Button("Видалити");
        Button manageButton = new Button("Редагувати");

        deleteButton.getStyleClass().add("miniButton");
        manageButton.getStyleClass().add("miniButton");

        deleteButton.setStyle("-fx-background-color: #41424A");

        BorderPane testCard = new BorderPane();
        VBox buttonVbox = new VBox(manageButton, deleteButton);
        buttonVbox.setStyle("-fx-spacing: 20; -fx-alignment: center");

        testCard.setLeft(textLabel);
        testCard.setRight(buttonVbox);

        testCard.setPrefWidth(1000.0);

        testCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return testCard;
    }

    @FXML
    public void handlerCreateQuestion() {
        String fxmlFile = "view/question/CreateQuestion.fxml";

        mainController.switchPage(fxmlFile);
    }

    public Test getCurrentTest() {
        return currentTest;
    }

    public void setCurrentTest(Test currentTest) {
        this.currentTest = currentTest;
    }
}
