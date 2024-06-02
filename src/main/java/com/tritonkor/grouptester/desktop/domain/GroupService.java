package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.GroupController;
import com.tritonkor.grouptester.desktop.net.request.group.ChangeUserStatusRequest;
import com.tritonkor.grouptester.desktop.net.request.group.ChooseTestRequest;
import com.tritonkor.grouptester.desktop.net.request.group.CreateGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.DeleteGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.JoinGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.LeaveGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.group.RunTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Group;
import java.net.http.HttpResponse;
import java.util.Map;
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

    public static HttpResponse<String> chooseTest(ChooseTestRequest request) {
        return GroupController.chooseTest(requestToJson(request));
    }

    public static HttpResponse<String> changeUserStatus(ChangeUserStatusRequest request) {
        return GroupController.changeUserStatus(requestToJson(request));
    }

    public static HttpResponse<String> getGroupStatus(Map<String, String> filters) {
        return GroupController.groupStatus(filters);
    }

    public static HttpResponse<String> runTest(RunTestRequest request) {
        return GroupController.runTest(requestToJson(request));
    }

    public static Group getCurrentGroup() {
        return currentGroup;
    }

    public static void setCurrentGroup(
            Group currentGroup) {
        GroupService.currentGroup = currentGroup;
    }
}
