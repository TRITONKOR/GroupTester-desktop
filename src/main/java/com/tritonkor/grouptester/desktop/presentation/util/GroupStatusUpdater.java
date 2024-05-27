package com.tritonkor.grouptester.desktop.presentation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.presentation.controller.group.CreateGroupController;
import com.tritonkor.grouptester.desktop.presentation.controller.group.ManageGroupController;
import jakarta.annotation.PreDestroy;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupStatusUpdater {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final GroupService groupService;
    private final CreateGroupController createGroupController;
    private final ManageGroupController manageGroupController;

    static {
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(User.class, new UserKeyDeserializer());
        objectMapper.registerModule(module);
    }

    @Autowired
    public GroupStatusUpdater(GroupService groupService, CreateGroupController createGroupController, ManageGroupController manageGroupController) {
        this.groupService = groupService;
        this.createGroupController = createGroupController;
        this.manageGroupController = manageGroupController;
    }

    public void startUpdating()  {
        scheduler.scheduleAtFixedRate(this::updateGroupStatus, 0, 2, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stopUpdating() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    private void updateGroupStatus() {
        UUID groupId = GroupService.getCurrentGroup().getId();
        Map<String, String> filters = new HashMap<>();
        filters.put("groupId", groupId.toString());

        HttpResponse<String> response = groupService.getGroupStatus(filters);
        System.out.println(response.body());
        try {

        Group group;
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            group = objectMapper.readValue(jsonResponse, new TypeReference<Group>() {});
        } else {
            // Обробка помилки або повернення порожнього списку
            throw new RuntimeException("Failed to fetch group: " + response.statusCode());
        }
        if (group != null) {
            Platform.runLater(() -> {
                GroupService.setCurrentGroup(group);
                try {
                    manageGroupController.updateUserList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
