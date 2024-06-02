package com.tritonkor.grouptester.desktop.presentation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void startUpdating() {
        if (scheduler.isShutdown() || scheduler.isTerminated()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }
        scheduler.scheduleAtFixedRate(this::updateGroupStatus, 0, 2, TimeUnit.SECONDS);
    }

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

    private void checkGroupExist() {
        if (Objects.isNull(GroupService.getCurrentGroup())) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Група не існує");
            alert.setHeaderText(null);
            alert.setContentText("Даної групи не існує");
            alert.showAndWait();

            String fxml = "view/test/main_student.fxml";
            mainController.setPage(fxml, manageGroupController.readyButton.getScene());
        }
    }

    private void updateGroupStatus() {
        checkGroupExist();

        UUID groupId = GroupService.getCurrentGroup().getId();
        Map<String, String> filters = new HashMap<>();
        filters.put("groupId", groupId.toString());

        HttpResponse<String> response = GroupService.getGroupStatus(filters);
        try {
            Group group;
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                group = objectMapper.readValue(jsonResponse, new TypeReference<Group>() {
                });
            } else {
                throw new RuntimeException("Failed to fetch group: " + response.statusCode());
            }
            if (group != null) {
                Platform.runLater(() -> {
                    GroupService.setCurrentGroup(group);
                    if (!group.getCanApplyNewUsers() && AuthorizeService.getCurrentUser().getRole().equals(Role.TEACHER)) {
                        manageGroupController.isTesting = true;
                        manageGroupController.runTestButton.setVisible(false);
                        manageGroupController.chooseTestButton.setVisible(false);
                    }
                    updateGroupData(group);
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void updateTestingData(Group group) {
        try {
            manageGroupController.updateUserList();
            manageGroupController.updateGroupData(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private boolean userChecker(Group group) {
        return group.getUsers().values().stream().allMatch(Boolean::booleanValue);
    }
}
