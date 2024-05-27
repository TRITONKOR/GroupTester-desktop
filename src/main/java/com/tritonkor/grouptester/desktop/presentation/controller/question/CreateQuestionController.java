package com.tritonkor.grouptester.desktop.presentation.controller.question;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class CreateQuestionController {
    @FXML
    private TextField questionTextField;
    @FXML
    private ToggleGroup answersGroup;

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузити зображення");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        fileChooser.showOpenDialog(new Stage());
        // Обробка вибраного файлу
    }

    @FXML
    private void handleSave() {
        String question = questionTextField.getText();
        RadioButton selectedAnswer = (RadioButton) answersGroup.getSelectedToggle();
        // Обробка збереження питання та вибраної відповіді
    }
}
