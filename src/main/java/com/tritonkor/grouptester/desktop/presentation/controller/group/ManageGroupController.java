package com.tritonkor.grouptester.desktop.presentation.controller.group;

import static com.tritonkor.grouptester.desktop.App.springContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tritonkor.grouptester.desktop.App;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.net.request.group.ChangeUserStatusRequest;
import com.tritonkor.grouptester.desktop.net.request.group.DeleteGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.LeaveGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.RunTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import com.tritonkor.grouptester.desktop.persistence.entity.Mark;
import com.tritonkor.grouptester.desktop.persistence.entity.Result;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.util.GroupStatusUpdater;
import com.tritonkor.grouptester.desktop.presentation.util.SpringFXMLLoader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    private Label testTitleLabel;
    @FXML
    private VBox vboxContainer;

    @FXML
    public Button readyButton;
    @FXML
    public Button runTestButton;
    @FXML
    public Button chooseTestButton;

    public Boolean isTesting;

    @FXML
    public void initialize() throws JsonProcessingException {
        isTesting = false;
        updateGroupData(GroupService.getCurrentGroup());
        updateUserList();
        groupStatusUpdater.startUpdating();
    }

    public void updateGroupData(Group group) {
        groupLabel.setText("Група: " + group.getName());
        codeLabel.setText("Код групи: " + group.getCode());

        testTitleLabel.setText("Тест: ");
        if (Objects.nonNull(group.getTest())) {
            testTitleLabel.setText("Тест: " + group.getTest().getTitle());
        }
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
        groupStatusUpdater.stopUpdating();

        DeleteGroupRequest request = DeleteGroupRequest.builder()
                .userId(AuthorizeService.getCurrentUser().getId())
                .groupId(GroupService.getCurrentGroup().getId())
                .build();

        GroupService.deleteGroup(request);
        GroupService.setCurrentGroup(null);

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

    public void updateUserList() {
        Map<User, Boolean> users = GroupService.getCurrentGroup().getUsers();
        Map<User, Mark> results = GroupService.getCurrentGroup().getResults();

        vboxContainer.getChildren().clear();
        if (!isTesting) {
            vboxContainer.getChildren().addAll(
                    users.entrySet().stream()
                            .map(entry -> createUserCard(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList())
            );
        } else {
            vboxContainer.getChildren().addAll(
                    results.entrySet().stream()
                            .map(entry -> createUserCard(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList())
            );
        }
    }

    private BorderPane createUserCard(User user, Mark mark) {
        byte[] userAvatar = AuthorizeService.getCurrentUser().getAvatar();
        InputStream inputStream = new ByteArrayInputStream(userAvatar);
        Image avatar = new Image(inputStream);
        ImageView imageView = new ImageView(avatar);
        imageView.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;");

        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20");

        String status;


        status = mark != null ? "Виконав: " + mark : "Виконує";


        Text statusText = new Text("Статус: ");
        statusText.setFill(Color.WHITE);
        statusText.setStyle("-fx-font-size: 16");

        Text dynamicStatusText = new Text(status);
        dynamicStatusText.setFill(mark != null ? Color.GREEN : Color.RED);
        dynamicStatusText.setStyle("-fx-font-size: 16");

        TextFlow statusBox = new TextFlow(statusText, dynamicStatusText);

        BorderPane userCard = new BorderPane();
        HBox containerHbox = new HBox(imageView, usernameLabel);

        BorderPane.setAlignment(statusBox, Pos.CENTER_RIGHT);

        userCard.setLeft(containerHbox);
        userCard.setRight(statusBox);

        userCard.setPrefWidth(1000.0);

        userCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return userCard;
    }

    private BorderPane createUserCard(User user, Boolean isReady) {
        byte[] userAvatar = AuthorizeService.getCurrentUser().getAvatar();
        InputStream inputStream = new ByteArrayInputStream(userAvatar);
        Image avatar = new Image(inputStream);
        ImageView imageView = new ImageView(avatar);
        imageView.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;");

        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20");

        String status;


        status = isReady ? "Готовий" : "Не готовий";


        Text statusText = new Text("Статус: ");
        statusText.setFill(Color.WHITE);
        statusText.setStyle("-fx-font-size: 16");

        Text dynamicStatusText = new Text(status);
        dynamicStatusText.setFill(isReady ? Color.GREEN : Color.RED);
        dynamicStatusText.setStyle("-fx-font-size: 16");

        TextFlow statusBox = new TextFlow(statusText, dynamicStatusText);

        BorderPane userCard = new BorderPane();
        HBox containerHbox = new HBox(imageView, usernameLabel);

        BorderPane.setAlignment(statusBox, Pos.CENTER_RIGHT);

        userCard.setLeft(containerHbox);
        userCard.setRight(statusBox);

        userCard.setPrefWidth(1000.0);

        userCard.setStyle(
                "-fx-padding: 10; -fx-background-color: #212121; -fx-background-radius: 10;");

        return userCard;
    }

    @FXML
    public void handleChooseTest() {
        String fxml = "view/group/ChooseTest.fxml";
        mainController.setPage(fxml, groupLabel.getScene());
    }

    @FXML
    public void handleChangeStatus() {
        ChangeUserStatusRequest request = ChangeUserStatusRequest.builder()
                .userId(AuthorizeService.getCurrentUser().getId())
                .groupCode(GroupService.getCurrentGroup().getCode())
                .build();

        GroupService.changeUserStatus(request);

        if (readyButton.getText().equals("Готовий")) {
            readyButton.setText("Не готовий");
        } else {
            readyButton.setText("Готовий");
        }
    }

    @FXML
    public void handleRuntTest() {
        RunTestRequest request = RunTestRequest.builder()
                .userID(AuthorizeService.getCurrentUser().getId())
                .groupId(GroupService.getCurrentGroup().getId())
                .build();

        GroupService.runTest(request);
    }
}
