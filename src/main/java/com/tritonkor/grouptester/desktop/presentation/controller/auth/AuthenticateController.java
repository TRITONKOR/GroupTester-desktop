package com.tritonkor.grouptester.desktop.presentation.controller.auth;

import static com.tritonkor.grouptester.desktop.App.springContext;

import com.tritonkor.grouptester.desktop.App;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.net.controller.AuthController;
import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateController {
    @Autowired
    private AuthorizeService authorizeService;

    @FXML
    private Hyperlink registerHyperLink;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleAuthenticateUser() {
        String username = loginField.getText();
        String password = passwordField.getText();

        authorizeService.authenticate(username, password);
        if (Objects.nonNull(authorizeService.getCurrentUser())) {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            URL mainFxmlResource;
            if (AuthorizeService.getCurrentUser().getRole().equals(Role.TEACHER)) {
                mainFxmlResource = App.class.getResource(
                        "view/main_teacher.fxml");
            } else {
                mainFxmlResource = App.class.getResource(
                        "view/main_student.fxml");
            }
            try {
                BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);
                Scene scene = registerHyperLink.getScene();
                scene.setRoot(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRegisterHyperlink(ActionEvent event) {
        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            var mainFxmlResource = App.class.getResource(
                    "view/authorize/register.fxml");
            BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);
            Scene scene = registerHyperLink.getScene();
            scene.setRoot(parent);

            ImageView backgroundImage = (ImageView) parent.lookup("#backgroundImage");

            backgroundImage.fitWidthProperty().bind(parent.widthProperty());
            backgroundImage.fitHeightProperty().bind(parent.heightProperty());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
