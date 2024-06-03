package com.tritonkor.grouptester.desktop.presentation.controller.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.ResultService;
import com.tritonkor.grouptester.desktop.net.response.ResultResponse;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.controller.question.QuestionListController;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultListByGroupController {
    @Autowired
    private MainController mainController;
    @Autowired
    private QuestionListController questionListController;

    @FXML
    private ScrollPane resultsScrollPane;
    @FXML
    private VBox vboxContainer;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        updateResultList();
    }

    public void updateResultList() throws JsonProcessingException {
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", AuthorizeService.getCurrentUser().getId().toString());
        HttpResponse<String> response = ResultService.getResultsByTeacher(filters);
        List<ResultResponse> results;

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            results = objectMapper.readValue(jsonResponse, new TypeReference<List<ResultResponse>>() {});
        } else {
            throw new RuntimeException("Failed to fetch results: " + response.statusCode());
        }

        Map<String, List<ResultResponse>> groupedResults = results.stream()
                .collect(Collectors.groupingBy(ResultResponse::getGroupCode));

        vboxContainer.getChildren().clear();

        groupedResults.forEach((groupCode, resultList) -> {
            vboxContainer.getChildren().add(createResultCard(resultList));
        });
    }

    private BorderPane createResultCard(List<ResultResponse> results) {
        Label testTitleLabel = new Label("Тест: " + results.get(0).getTest().getTitle());
        testTitleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 22");

        Label groupCodeLabel = new Label("Група: " + results.get(0).getGroupCode());
        groupCodeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 22");

        Label countLabel = new Label("Кількість: " + results.size());
        countLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18");

        Button resultsButton = new Button("Результати");

        resultsButton.setOnAction((event -> handleResults(results)));

        resultsButton.getStyleClass().add("miniButton");

        BorderPane resultCard = new BorderPane();
        VBox labelVbox = new VBox(testTitleLabel, groupCodeLabel, countLabel);

        BorderPane.setAlignment(resultsButton, Pos.CENTER_RIGHT);

        resultCard.setLeft(labelVbox);
        resultCard.setRight(resultsButton);

        resultCard.setPrefWidth(1000.0);

        resultCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return resultCard;
    }

    private void handleResults(List<ResultResponse> results) {
        ResultService.setResults(results);

        String fxmlFile = "view/result/ResultList.fxml";
        mainController.switchPage(fxmlFile);
    }
}
