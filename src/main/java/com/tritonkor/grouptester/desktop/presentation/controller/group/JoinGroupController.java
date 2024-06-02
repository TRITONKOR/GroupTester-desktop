package com.tritonkor.grouptester.desktop.presentation.controller.group;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.net.request.group.CreateGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.JoinGroupRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.util.UserKeyDeserializer;
import java.io.IOException;
import java.net.http.HttpResponse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JoinGroupController {
    @Autowired
    private MainController mainController;

    @FXML
    private TextField groupCodeField;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(User.class, new UserKeyDeserializer());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(module);
    }

    @FXML
    private void handleJoinGroup() throws IOException {
        JoinGroupRequest request = JoinGroupRequest.builder()
                .code(groupCodeField.getText())
                .userId(AuthorizeService.getCurrentUser().getId())
                .build();

        HttpResponse<String> response = GroupService.joinGroup(request);

        Group group;
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            group = objectMapper.readValue(jsonResponse, new TypeReference<Group>() {});
        } else {
            // Обробка помилки або повернення порожнього списку
            throw new RuntimeException("Failed to fetch group: " + response.statusCode());
        }
        GroupService.setCurrentGroup(group);

        String fxmlFile = "view/group/ManageGroup-student.fxml";
        mainController.setPage(fxmlFile, groupCodeField.getScene());
    }
}
