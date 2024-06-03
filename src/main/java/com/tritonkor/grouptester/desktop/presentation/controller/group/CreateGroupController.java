package com.tritonkor.grouptester.desktop.presentation.controller.group;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.net.request.group.CreateGroupRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import com.tritonkor.grouptester.desktop.presentation.util.UserKeyDeserializer;
import java.net.http.HttpResponse;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateGroupController {

    @Autowired
    private MainController mainController;

    @FXML
    private TextField groupNameField;
    @FXML
    private TextField groupCodeField;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(User.class, new UserKeyDeserializer());
        objectMapper.registerModule(module);
    }

    @FXML
    private void handleCreateGroup() throws IOException {
        CreateGroupRequest request = CreateGroupRequest.builder()
                .name(groupNameField.getText())
                .code(groupCodeField.getText())
                .userId(AuthorizeService.getCurrentUser().getId())
                .build();

        HttpResponse<String> response = GroupService.createGroup(request);

        Group group;
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            try {

                group = objectMapper.readValue(jsonResponse, new TypeReference<Group>() {
                });
                GroupService.setCurrentGroup(group);
            } catch (MismatchedInputException e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Група з таким іменем або кодом вже існує");
                alert.setHeaderText(null);
                alert.setContentText("Змініть ім'я або код");
                alert.showAndWait();
            }
        } else {
            throw new RuntimeException("Failed to fetch group: " + response.statusCode());
        }

        String fxmlFile = "view/group/ManageGroup-teacher.fxml";
        mainController.setPage(fxmlFile, groupNameField.getScene());
    }
}
