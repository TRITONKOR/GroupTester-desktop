package com.tritonkor.grouptester.desktop.presentation.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.TagService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.request.test.CreateTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Tag;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateTestController {

    @Autowired
    private MainController mainController;

    @FXML
    private TextField testTitleField;
    @FXML
    private Spinner<Integer> timeSpinner;
    @FXML
    private CheckComboBox<Tag> tagCheckComboBox;

    private List<Tag> tags = new ArrayList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        HttpResponse<String> response = TagService.getAllTags();

        try {

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                tags = objectMapper.readValue(jsonResponse, new TypeReference<List<Tag>>() {
                });
            } else {
                // Обробка помилки або повернення порожнього списку
                throw new RuntimeException("Failed to fetch tests: " + response.statusCode());
            }
        } catch (JsonProcessingException e) {
        }
        if (!tags.isEmpty()) {
            tagCheckComboBox.getItems().addAll(tags);
        }
    }

    @FXML
    public void handleCreateTest() {
        String testTitle = testTitleField.getText();
        Integer time = timeSpinner.getValue();
        List<Tag> selectedTags = new ArrayList<>(tagCheckComboBox.getCheckModel().getCheckedItems());


        CreateTestRequest request = CreateTestRequest.builder()
                .userId(AuthorizeService.getCurrentUser().getId())
                .testTitle(testTitle)
                .tags(selectedTags)
                .time(time)
                .build();

        TestService.createTest(request);

        String fxml = "view/test/TestList.fxml";
        mainController.switchPage(fxml);
    }
}
