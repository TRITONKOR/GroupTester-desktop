package com.tritonkor.grouptester.desktop.presentation.controller.auth;

import static com.tritonkor.grouptester.desktop.App.springContext;

import com.tritonkor.grouptester.desktop.App;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.FileService;
import com.tritonkor.grouptester.desktop.net.request.RegisterRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import com.tritonkor.grouptester.desktop.presentation.viewmodel.UserViewModel;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller for the user registration view.
 * Handles user interactions for registering a new user.
 */
@Component
public class RegisterController {
    @Autowired
    private FileService fileService;

    @FXML
    private Hyperlink authenticateHyperLink;
    @FXML
    private TextField loginField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private ImageView avatarImage;
    @FXML
    private DatePicker birthdayDatePicker;

    private UserViewModel userViewModel;

    /**
     * Initializes the controller and binds UI fields to the view model.
     *
     * @throws IOException if an I/O error occurs during initialization.
     */
    @FXML
    public void initialize() throws IOException {
        roleComboBox.getItems().addAll(Role.values());
        roleComboBox.setValue(Role.STUDENT);

        userViewModel = new UserViewModel(
                UUID.randomUUID(),
                "JohnDoe",
                "john.doe@example.com",
                "password",
                new Image(fileService.getPathFromResource(
                        "com/tritonkor/grouptester/desktop/view/authorize/avatar.png").toUri().toString()),
                fileService.getPathFromResource(
                        "com/tritonkor/grouptester/desktop/view/authorize/avatar.png"),
                LocalDate.of(1990, 1, 1),
                Role.STUDENT
        );

        bindFieldsToViewModel();
    }

    /**
     * Binds the UI fields to the corresponding properties in the view model.
     */
    private void bindFieldsToViewModel() {
        loginField.textProperty().bindBidirectional(userViewModel.usernameProperty());
        emailField.textProperty().bindBidirectional(userViewModel.emailProperty());
        passwordField.textProperty().bindBidirectional(userViewModel.passwordProperty());
        avatarImage.imageProperty().bindBidirectional(userViewModel.avatarProperty());
        birthdayDatePicker.valueProperty().bindBidirectional(userViewModel.birthdayProperty());
        roleComboBox.valueProperty().bindBidirectional(userViewModel.roleProperty());
    }

    /**
     * Handles the user registration process.
     *
     * @throws IOException if an I/O error occurs during registration.
     */
    @FXML
    private void handleRegisterUser() throws IOException {
        if (userViewModel.getUsername().isBlank() || userViewModel.getPassword().isBlank() || userViewModel.getEmail().isBlank()
         || userViewModel.getBirthday() == null || userViewModel.getRole().getName().isBlank() || userViewModel.getAvatarPath() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Не вдалось зареєструватись");
            alert.setHeaderText(null);
            alert.setContentText("Не всі поля були заповнені");
            alert.showAndWait();
            return;
        }

        RegisterRequest request = RegisterRequest.builder()
                .username(userViewModel.getUsername())
                .password(userViewModel.getPassword())
                .email(userViewModel.getEmail())
                .birthday(userViewModel.getBirthday())
                .role(userViewModel.getRole())
                .avatar(fileService.getBytes(userViewModel.getAvatarPath()))
                .build();

        AuthorizeService.register(request);

        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            var mainFxmlResource = App.class.getResource(
                    "view/main_teacher.fxml");
            BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);
            Scene scene = authenticateHyperLink.getScene();
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when the avatar image is clicked.
     * Opens a file chooser to select a new avatar image.
     */
    @FXML
    private void handleAvatarImageClick() {
        FileChooser fileChooser = new FileChooser();
        var extensionFilter = new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        Path path = fileChooser.showOpenDialog(null).toPath();

        if (!path.toString().isBlank()) {
            Image image = new Image(path.toUri().toString());
            userViewModel.setAvatar(image);
            userViewModel.setAvatarPath(path);

            avatarImage.setImage(image);
        }
    }

    /**
     * Handles the event when the authenticate hyperlink is clicked.
     * Loads the authentication view.
     *
     * @param event the action event triggered by clicking the hyperlink.
     */
    @FXML
    private void handleAuthenticateHyperlink(ActionEvent event) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            var mainFxmlResource = App.class.getResource(
                    "view/authorize/authenticate.fxml");
            BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);
            Scene scene = authenticateHyperLink.getScene();
            scene.setRoot(parent);

            ImageView backgroundImage = (ImageView) parent.lookup("#backgroundImage");

            backgroundImage.fitWidthProperty().bind(parent.widthProperty());
            backgroundImage.fitHeightProperty().bind(parent.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
