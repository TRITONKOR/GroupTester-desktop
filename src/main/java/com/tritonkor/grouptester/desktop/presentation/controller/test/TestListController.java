package com.tritonkor.grouptester.desktop.presentation.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;

import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.controller.question.QuestionListController;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestListController {
    @Autowired
    private MainController mainController;
    @Autowired
    private QuestionListController questionListController;

    @FXML
    private ScrollPane testsScrollPane;
    @FXML
    private Button createTestButton;
    @FXML
    private VBox vboxContainer;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @FXML
    public void initialize() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        updateTestList();
    }

    public void updateTestList() throws JsonProcessingException {
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", "acf647ec-b69c-43cc-86fd-96628a1fb40a");
        HttpResponse<String> response = TestController.getAllTestsByUser(filters);
        List<Test> tests;

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            tests = objectMapper.readValue(jsonResponse, new TypeReference<List<Test>>() {});
        } else {
            // Обробка помилки або повернення порожнього списку
            throw new RuntimeException("Failed to fetch tests: " + response.statusCode());
        }

        vboxContainer.getChildren().clear();
        vboxContainer.getChildren().addAll(tests.stream().map(this::createTestCard).toList());
    }

    private BorderPane createTestCard(Test test) {
        Label titleLabel = new Label(test.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20");

        Label countOfQuestionsLabel = new Label("Кількість питань: " + test.getQuestions().size());
        countOfQuestionsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16");

        Label createDateLabel = new Label("Дата створення: " + test.getCreateDate().toString());
        createDateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16");

        Button deleteButton = new Button("Видалити");
        Button questionsButton = new Button("Питання");

        questionsButton.setOnAction(event -> handleQuestions(test));

        deleteButton.getStyleClass().add("miniButton");
        questionsButton.getStyleClass().add("miniButton");

        deleteButton.setStyle("-fx-background-color: #41424A");

        BorderPane testCard = new BorderPane();
        VBox labelVbox = new VBox(titleLabel, countOfQuestionsLabel, createDateLabel);
        VBox buttonVbox = new VBox(questionsButton, deleteButton);
        buttonVbox.setStyle("-fx-spacing: 20; -fx-alignment: center");

        testCard.setLeft(labelVbox);
        testCard.setRight(buttonVbox);

        testCard.setPrefWidth(1000.0);

        testCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return testCard;
    }

    private void handleQuestions(Test test) {
        questionListController.setCurrentTest(test);

        String fxmlFile = "view/question/QuestionList.fxml";

        mainController.switchPage(fxmlFile);
    }
}
