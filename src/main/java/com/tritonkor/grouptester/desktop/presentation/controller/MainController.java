package com.tritonkor.grouptester.desktop.presentation.controller;

import static com.tritonkor.grouptester.desktop.App.springContext;

import com.tritonkor.grouptester.desktop.App;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Component;


@Component
public class MainController {

    @FXML
    private Label titleLabel;
    @FXML
    private BorderPane root;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private ImageView userAvatarImageView;
    @FXML
    private Label usernameLabel;

    @FXML
    public void initialize() throws IOException {
        loadUserData();
    }

    @FXML
    private void handleMenuSelection(ActionEvent actionEvent) {
        ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
        if (selectedButton != null) {
            switch (selectedButton.getText()) {
                case "Група" -> switchPage(Path.of("view", "group", "CreateGroup.fxml").toString());
                case "Групи" -> switchPage(Path.of("view", "group", "JoinGroup.fxml").toString());
                case "Бібліотека" ->
                        switchPage(Path.of("view", "test", "TestList.fxml").toString());
                case "Звіти" -> switchPage(Path.of("view", "report", "ReportList.fxml").toString());
                case "Результати" -> switchPage(Path.of("view", "result", "ResultList.fxml").toString());
                default -> System.err.println(STR."Unknown selection: \{selectedButton.getText()}");
            }
        }
    }

    public void switchPage(String fxmlFile) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            Pane newPage = (Pane) fxmlLoader.load(App.class.getResource(fxmlFile));
            root.setCenter(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPage(String fxmlFile) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            BorderPane parent = (BorderPane) fxmlLoader.load(App.class.getResource(fxmlFile));
            Scene scene = titleLabel.getScene();
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserData() throws IOException {
        usernameLabel.setText(AuthorizeService.getCurrentUser().getUsername());

        byte[] userAvatar = AuthorizeService.getCurrentUser().getAvatar();
        InputStream inputStream = new ByteArrayInputStream(userAvatar);
        Image avatar = new Image(inputStream);
        userAvatarImageView.setImage(avatar);
    }
}
