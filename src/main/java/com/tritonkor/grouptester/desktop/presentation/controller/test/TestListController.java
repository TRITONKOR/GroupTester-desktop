package com.tritonkor.grouptester.desktop.presentation.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.net.request.question.DeleteQuestionRequest;
import com.tritonkor.grouptester.desktop.net.request.test.DeleteTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import com.tritonkor.grouptester.desktop.persistence.entity.Tag;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;

import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.controller.question.QuestionListController;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public void updateTestList()  {
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", AuthorizeService.getCurrentUser().getId().toString());
        HttpResponse<String> response = TestController.getAllTestsByUser(filters);
        List<Test> tests = new ArrayList<>();
        try {

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                tests = objectMapper.readValue(jsonResponse, new TypeReference<List<Test>>() {
                });
            } else {
                throw new RuntimeException("Failed to fetch tests: " + response.statusCode());
            }
        } catch (JsonProcessingException e) {}

        vboxContainer.getChildren().clear();
        vboxContainer.getChildren().addAll(tests.stream().map(this::createTestCard).toList());
    }

    private BorderPane createTestCard(Test test) {
        Label titleLabel = new Label(test.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 22");

        Label countOfQuestionsLabel = new Label("Кількість питань: " + test.getQuestions().size());
        countOfQuestionsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18");

        Label createDateLabel = new Label("Дата створення: " + test.getCreateDate().toString());
        createDateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18");

        Label tagsLabel = new Label();
        for (Tag tag : test.getTags())
        {
            tagsLabel.setText(tagsLabel.getText() + tag.toString() + "   ");
        }
        tagsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18");

        Button deleteButton = new Button("Видалити");
        Button questionsButton = new Button("Питання");

        questionsButton.setOnAction(event -> handleQuestions(test));
        deleteButton.setOnAction(event -> handleDeleteTest(test));

        deleteButton.getStyleClass().add("miniButton");
        questionsButton.getStyleClass().add("miniButton");

        deleteButton.setStyle("-fx-background-color: #41424A");

        BorderPane testCard = new BorderPane();
        VBox labelVbox = new VBox(titleLabel, countOfQuestionsLabel, tagsLabel, createDateLabel);
        VBox buttonVbox = new VBox(questionsButton, deleteButton);
        buttonVbox.setStyle("-fx-spacing: 10; -fx-alignment: center");

        testCard.setLeft(labelVbox);
        testCard.setRight(buttonVbox);

        testCard.setPrefWidth(1000.0);

        testCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return testCard;
    }

    private void handleQuestions(Test test) {
        TestService.setCurrentTest(test);

        String fxmlFile = "view/question/QuestionList.fxml";
        mainController.switchPage(fxmlFile);
    }

    public void handleDeleteTest(Test test)  {
        DeleteTestRequest request = DeleteTestRequest.builder()
                .testId(test.getId())
                .userId(AuthorizeService.getCurrentUser().getId())
                .build();

        TestService.deleteTest(request);
        updateTestList();
    }

    @FXML
    private void handleQuestions() {
        String fxmlFile = "view/test/CreateTest.fxml";
        mainController.switchPage(fxmlFile);
    }
}
