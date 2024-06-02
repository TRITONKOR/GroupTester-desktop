package com.tritonkor.grouptester.desktop.presentation.controller.auth;

import static com.tritonkor.grouptester.desktop.App.springContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.App;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.FileService;
import com.tritonkor.grouptester.desktop.net.controller.AuthController;
import com.tritonkor.grouptester.desktop.net.request.RegisterRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import com.tritonkor.grouptester.desktop.presentation.viewmodel.UserViewModel;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

    private UserViewModel userViewModel;

    @FXML
    public void initialize() throws IOException {
        roleComboBox.getItems().addAll(Role.values());
        roleComboBox.setValue(Role.STUDENT);

        // Створення користувача з пустими даними як приклад
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

        // Зв'язування властивостей ViewModel з View
        bindFieldsToViewModel();
    }

    private void bindFieldsToViewModel() {
        loginField.textProperty().bindBidirectional(userViewModel.usernameProperty());
        emailField.textProperty().bindBidirectional(userViewModel.emailProperty());
        passwordField.textProperty().bindBidirectional(userViewModel.passwordProperty());
        avatarImage.imageProperty().bindBidirectional(userViewModel.avatarProperty());
        //birthdayPicker.valueProperty().bindBidirectional(userViewModel.birthdayProperty());
        roleComboBox.valueProperty().bindBidirectional(userViewModel.roleProperty());
    }

    @FXML
    private void handleRegisterUser() throws IOException {
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

            // Встановлюємо вибране фото як зображення для ImageView
            avatarImage.setImage(image);
        }
    }
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
