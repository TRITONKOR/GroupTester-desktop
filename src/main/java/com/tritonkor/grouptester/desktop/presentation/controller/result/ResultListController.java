package com.tritonkor.grouptester.desktop.presentation.controller.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.ResultService;
import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.net.response.ResultResponse;
import com.tritonkor.grouptester.desktop.persistence.entity.Result;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.controller.question.QuestionListController;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ResultListController {
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
        HttpResponse<String> response = ResultService.getResultsByUser(filters);
        List<ResultResponse> results;

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            System.out.println(jsonResponse);
            results = objectMapper.readValue(jsonResponse, new TypeReference<List<ResultResponse>>() {});
        } else {
            // Обробка помилки або повернення порожнього списку
            throw new RuntimeException("Failed to fetch results: " + response.statusCode());
        }

        vboxContainer.getChildren().clear();
        vboxContainer.getChildren().addAll(results.stream().map(this::createResultCard).toList());
    }

    private BorderPane createResultCard(ResultResponse result) {
        Label titleLabel = new Label(result.getTest().getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20");

        Label markLabel = new Label("Оцінка: " + result.getMark());
        markLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16");

        Label createDateLabel = new Label("Дата створення: " + result.getCreate_date().toString());
        createDateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16");


        BorderPane resultCard = new BorderPane();
        VBox labelVbox = new VBox(titleLabel, createDateLabel);

        BorderPane.setAlignment(markLabel, Pos.CENTER_RIGHT);

        resultCard.setLeft(labelVbox);
        resultCard.setRight(markLabel);

        resultCard.setPrefWidth(1000.0);

        resultCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return resultCard;
    }
}
