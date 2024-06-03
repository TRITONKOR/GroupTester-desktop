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

/**
 * Controller for the main view of the application.
 */
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

    /**
     * Initializes the controller after its root element has been completely processed.
     * Loads user data upon initialization.
     *
     * @throws IOException if an I/O error occurs while loading user data.
     */
    @FXML
    public void initialize() throws IOException {
        loadUserData();
    }

    /**
     * Handles menu selection events.
     *
     * @param actionEvent the ActionEvent representing the menu selection.
     */
    @FXML
    private void handleMenuSelection(ActionEvent actionEvent) {
        ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
        if (selectedButton != null) {
            switch (selectedButton.getText()) {
                case "Група" -> switchPage(Path.of("view", "group", "CreateGroup.fxml").toString());
                case "Групи" -> switchPage(Path.of("view", "group", "JoinGroup.fxml").toString());
                case "Бібліотека" ->
                        switchPage(Path.of("view", "test", "TestList.fxml").toString());
                case "Звіти" -> switchPage(Path.of("view", "result", "ResultListByGroup.fxml").toString());
                case "Результати" -> switchPage(Path.of("view", "result", "ResultList.fxml").toString());
                default -> System.err.println(STR."Unknown selection: \{selectedButton.getText()}");
            }
        }
    }

    /**
     * Switches the current page to the one specified by the FXML file path.
     *
     * @param fxmlFile the path to the FXML file representing the new page.
     */
    public void switchPage(String fxmlFile) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            Pane newPage = (Pane) fxmlLoader.load(App.class.getResource(fxmlFile));
            root.setCenter(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the scene to the specified FXML file.
     *
     * @param fxmlFile the path to the FXML file representing the new scene.
     * @param scene the Scene object to set.
     */
    public void setPage(String fxmlFile, Scene scene) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            BorderPane parent = (BorderPane) fxmlLoader.load(App.class.getResource(fxmlFile));
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads user data and populates the UI components with the user's information.
     *
     * @throws IOException if an I/O error occurs while loading user data.
     */
    private void loadUserData() throws IOException {
        usernameLabel.setText(AuthorizeService.getCurrentUser().getUsername());

        byte[] userAvatar = AuthorizeService.getCurrentUser().getAvatar();
        InputStream inputStream = new ByteArrayInputStream(userAvatar);
        Image avatar = new Image(inputStream);
        userAvatarImageView.setImage(avatar);
    }
}
