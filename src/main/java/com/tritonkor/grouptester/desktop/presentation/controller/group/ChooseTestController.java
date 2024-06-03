package com.tritonkor.grouptester.desktop.presentation.controller.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.net.request.group.ChooseTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.controller.question.QuestionListController;
import java.net.http.HttpResponse;
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
public class ChooseTestController {
    @Autowired
    private MainController mainController;

    @FXML
    private ScrollPane testsScrollPane;
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
        filters.put("userId", AuthorizeService.getCurrentUser().getId().toString());
        HttpResponse<String> response = TestController.getAllTestsByUser(filters);
        List<Test> tests;

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            tests = objectMapper.readValue(jsonResponse, new TypeReference<List<Test>>() {});
        } else {
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

        BorderPane testCard = new BorderPane();
        VBox labelVbox = new VBox(titleLabel, countOfQuestionsLabel, createDateLabel);

        testCard.setLeft(labelVbox);

        testCard.setPrefWidth(1000.0);

        testCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        testCard.setOnMouseClicked(event -> chooseTest(test));

        return testCard;
    }

    private void chooseTest(Test test) {
        ChooseTestRequest request =  ChooseTestRequest.builder()
                .userId(AuthorizeService.getCurrentUser().getId())
                .groupId(GroupService.getCurrentGroup().getId())
                .testId(test.getId())
                .build();

        GroupService.chooseTest(request);
        String fxml = "view/group/ManageGroup-teacher.fxml";
        mainController.setPage(fxml, testsScrollPane.getScene());
    }
}
