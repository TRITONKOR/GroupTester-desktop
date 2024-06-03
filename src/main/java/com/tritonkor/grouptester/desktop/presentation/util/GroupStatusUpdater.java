package com.tritonkor.grouptester.desktop.presentation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.request.group.DeleteGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.LeaveGroupRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.controller.group.ManageGroupController;
import jakarta.annotation.PreDestroy;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component responsible for updating group status in the GroupTester desktop application.
 */
@Component
public class GroupStatusUpdater {

    @Autowired
    private MainController mainController;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ManageGroupController manageGroupController;

    static {
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(User.class, new UserKeyDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Autowired
    public GroupStatusUpdater(ManageGroupController manageGroupController) {
        this.manageGroupController = manageGroupController;
    }

    /**
     * Starts the process of updating group status.
     */
    public void startUpdating() {
        if (scheduler.isShutdown() || scheduler.isTerminated()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }
        scheduler.scheduleAtFixedRate(this::updateGroupStatus, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Handles actions to be taken when the program is closing.
     */
    @PreDestroy
    public void programClosing() {
        if (AuthorizeService.getCurrentUser().getRole().equals(Role.STUDENT)) {
            LeaveGroupRequest request = LeaveGroupRequest.builder()
                    .code(GroupService.getCurrentGroup().getCode())
                    .userId(AuthorizeService.getCurrentUser().getId())
                    .build();

            GroupService.leaveGroup(request);
        } else {
            DeleteGroupRequest request = DeleteGroupRequest.builder()
                    .groupId(GroupService.getCurrentGroup().getId())
                    .userId(AuthorizeService.getCurrentUser().getId())
                    .build();

            GroupService.deleteGroup(request);
        }

        stopUpdating();
    }

    /**
     * Stops the process of updating group status.
     */
    public void stopUpdating() {
        if (Objects.nonNull(scheduler)) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }

    /**
     * Updates the group status by making requests to the backend.
     */
    private void updateGroupStatus() {
        UUID groupId = GroupService.getCurrentGroup().getId();
        Map<String, String> filters = new HashMap<>();
        filters.put("groupId", groupId.toString());

        HttpResponse<String> response = GroupService.getGroupStatus(filters);
        try {
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();

                try {
                    Group group = objectMapper.readValue(jsonResponse, new TypeReference<Group>() {
                    });
                    GroupService.setCurrentGroup(group);
                } catch (MismatchedInputException e) {
                    stopUpdating();

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Група не існує");
                    alert.setHeaderText(null);
                    alert.setContentText("Даної групи не існує");
                    alert.showAndWait();


                    String fxml = "view/test/main_student.fxml";
                    mainController.setPage(fxml, manageGroupController.readyButton.getScene());
                }
            } else {
                throw new RuntimeException("Failed to fetch group: " + response.statusCode());
            }
            if (GroupService.getCurrentGroup() != null) {
                Platform.runLater(() -> {
                    if (!GroupService.getCurrentGroup().getCanApplyNewUsers() && AuthorizeService.getCurrentUser().getRole().equals(Role.TEACHER)) {
                        manageGroupController.isTesting = true;
                        manageGroupController.runTestButton.setVisible(false);
                        manageGroupController.chooseTestButton.setVisible(false);
                    }
                    updateGroupData(GroupService.getCurrentGroup());
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the data of the current group.
     * @param group The updated group object.
     */
    private void updateGroupData(Group group) {
        try {
            if (Objects.nonNull(manageGroupController.runTestButton)) {
                if (userChecker(group) && Objects.nonNull(group.getTest())) {
                    manageGroupController.runTestButton.setDisable(false);
                } else {
                    manageGroupController.runTestButton.setDisable(true);
                }
            }
            if (Objects.nonNull(group.getTest())) {
                TestService.setCurrentTest(group.getTest());
            }

            manageGroupController.updateUserList();
            manageGroupController.updateGroupData(group);

            if (group.getReadyToTesting() && AuthorizeService.getCurrentUser()
                    .getRole().equals(
                            Role.STUDENT)) {
                stopUpdating();
                String fxml = "view/test/RunTest.fxml";
                mainController.setPage(fxml,
                        manageGroupController.readyButton.getScene());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if all users in the group are ready.
     * @param group The group to check.
     * @return True if all users are ready, false otherwise.
     */
    private boolean userChecker(Group group) {
        return group.getUsers().values().stream().allMatch(Boolean::booleanValue);
    }
}
