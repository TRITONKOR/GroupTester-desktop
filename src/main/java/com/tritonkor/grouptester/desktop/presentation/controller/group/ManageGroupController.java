package com.tritonkor.grouptester.desktop.presentation.controller.group;

import static com.tritonkor.grouptester.desktop.App.springContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tritonkor.grouptester.desktop.App;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.net.request.group.DeleteGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.JoinGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.LeaveGroupRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.util.GroupStatusUpdater;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import com.tritonkor.grouptester.desktop.presentation.util.UserKeyDeserializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManageGroupController {
    @Autowired
    private MainController mainController;
    @Autowired
    private GroupStatusUpdater groupStatusUpdater;

    @FXML
    private Label groupLabel;
    @FXML
    private Label codeLabel;
    @FXML
    private VBox vboxContainer;

    @FXML
    public void initialize() throws JsonProcessingException {

        updateGroupData(GroupService.getCurrentGroup());
        updateUserList();
        groupStatusUpdater.startUpdating();
    }

    public void updateGroupData(Group group) {
        groupLabel.setText(groupLabel.getText() + group.getName());
        codeLabel.setText(codeLabel.getText() + group.getCode());
    }

    @FXML
    public void handleLeaveGroup() {
        LeaveGroupRequest request = LeaveGroupRequest.builder()
                .userId(AuthorizeService.getCurrentUser().getId())
                .code(GroupService.getCurrentGroup().getCode())
                .build();

        GroupService.leaveGroup(request);

        groupStatusUpdater.stopUpdating();

        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            var mainFxmlResource = App.class.getResource(
                    "view/main_student.fxml");
            BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);
            Scene scene = groupLabel.getScene();
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteGroup() {
        DeleteGroupRequest request = DeleteGroupRequest.builder()
                .userId(AuthorizeService.getCurrentUser().getId())
                .groupId(GroupService.getCurrentGroup().getId())
                .build();

        GroupService.deleteGroup(request);
        GroupService.setCurrentGroup(null);

        groupStatusUpdater.stopUpdating();

        try {
            var fxmlLoader = new SpringFXMLLoader(springContext);
            var mainFxmlResource = App.class.getResource(
                    "view/main_teacher.fxml");
            BorderPane parent = (BorderPane) fxmlLoader.load(mainFxmlResource);
            Scene scene = groupLabel.getScene();
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserList() throws JsonProcessingException {
        Map<User, Boolean> users = GroupService.getCurrentGroup().getUsers();

        vboxContainer.getChildren().clear();
        vboxContainer.getChildren().addAll(
                users.entrySet().stream()
                        .map(entry -> createUserCard(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        );
    }

    private BorderPane createUserCard(User user, Boolean isReady) {
        byte[] userAvatar = AuthorizeService.getCurrentUser().getAvatar();
        InputStream inputStream = new ByteArrayInputStream(userAvatar);
        Image avatar = new Image(inputStream);
        ImageView imageView = new ImageView(avatar);
        imageView.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;");


        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20");

        String status = isReady ? "Готовий" : "Не готовий";

        Label isReadyLabel = new Label("Статус: " + status);
        isReadyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16");



        BorderPane userCard = new BorderPane();
        HBox containerHbox = new HBox(imageView, usernameLabel);

        BorderPane.setAlignment(isReadyLabel, Pos.CENTER_RIGHT);

        userCard.setLeft(containerHbox);
        userCard.setRight(isReadyLabel);

        userCard.setPrefWidth(1000.0);

        userCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return userCard;
    }
}
