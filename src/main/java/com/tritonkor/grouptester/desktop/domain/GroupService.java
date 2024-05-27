package com.tritonkor.grouptester.desktop.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.net.controller.GroupController;
import com.tritonkor.grouptester.desktop.net.request.Request;
import com.tritonkor.grouptester.desktop.net.request.group.CreateGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.DeleteGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.JoinGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.LeaveGroupRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class GroupService extends com.tritonkor.grouptester.desktop.domain.Service {
    private static Group currentGroup;

    public static boolean deleteGroup(DeleteGroupRequest request) {
        GroupController.deleteGroup(requestToJson(request));
        return true;
    }

    public static HttpResponse<String> createGroup(CreateGroupRequest request) {
        return GroupController.createGroup(requestToJson(request));
    }

    public static HttpResponse<String> joinGroup(JoinGroupRequest request) {
        return GroupController.joinGroup(requestToJson(request));
    }

    public static HttpResponse<String> leaveGroup(LeaveGroupRequest request) {
        return GroupController.leaveGroup(requestToJson(request));
    }

    public static HttpResponse<String> getGroupStatus(Map<String, String> filters) {
        return GroupController.groupStatus(filters);
    }

    public static Group getCurrentGroup() {
        return currentGroup;
    }

    public static void setCurrentGroup(
            Group currentGroup) {
        GroupService.currentGroup = currentGroup;
    }
}
