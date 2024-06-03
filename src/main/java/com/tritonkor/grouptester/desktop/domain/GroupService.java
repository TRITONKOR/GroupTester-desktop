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

/**
 * Service class for managing groups in the GroupTester desktop application.
 */
@Service
public class GroupService extends com.tritonkor.grouptester.desktop.domain.Service {
    private static Group currentGroup;

    /**
     * Deletes a group.
     *
     * @param request The request containing the group data.
     * @return True if the group was successfully deleted, false otherwise.
     */
    public static boolean deleteGroup(DeleteGroupRequest request) {
        GroupController.deleteGroup(requestToJson(request));
        return true;
    }

    /**
     * Creates a new group.
     *
     * @param request The request containing the group data.
     * @return The HTTP response indicating the result of the create operation.
     */
    public static HttpResponse<String> createGroup(CreateGroupRequest request) {
        return GroupController.createGroup(requestToJson(request));
    }

    /**
     * Joins a group.
     *
     * @param request The request containing the group data.
     * @return The HTTP response indicating the result of the join operation.
     */
    public static HttpResponse<String> joinGroup(JoinGroupRequest request) {
        return GroupController.joinGroup(requestToJson(request));
    }

    /**
     * Leaves a group.
     *
     * @param request The request containing the group data.
     * @return The HTTP response indicating the result of the leave operation.
     */
    public static HttpResponse<String> leaveGroup(LeaveGroupRequest request) {
        return GroupController.leaveGroup(requestToJson(request));
    }

    /**
     * Chooses a test for the group.
     *
     * @param request The request containing the test data.
     * @return The HTTP response indicating the result of the choose operation.
     */
    public static HttpResponse<String> chooseTest(ChooseTestRequest request) {
        return GroupController.chooseTest(requestToJson(request));
    }

    /**
     * Changes the status of a user in the group.
     *
     * @param request The request containing the status change data.
     * @return The HTTP response indicating the result of the status change operation.
     */
    public static HttpResponse<String> changeUserStatus(ChangeUserStatusRequest request) {
        return GroupController.changeUserStatus(requestToJson(request));
    }

    /**
     * Retrieves the status of the group.
     *
     * @param filters The filters to apply.
     * @return The HTTP response containing the status of the group.
     */
    public static HttpResponse<String> getGroupStatus(Map<String, String> filters) {
        return GroupController.groupStatus(filters);
    }

    /**
     * Runs a test for the group.
     *
     * @param request The request containing the test data.
     * @return The HTTP response indicating the result of the test run.
     */
    public static HttpResponse<String> runTest(RunTestRequest request) {
        return GroupController.runTest(requestToJson(request));
    }

    /**
     * Retrieves the current group.
     *
     * @return The current group.
     */
    public static Group getCurrentGroup() {
        return currentGroup;
    }

    /**
     * Sets the current group.
     *
     * @param currentGroup The group to set as the current group.
     */
    public static void setCurrentGroup(
            Group currentGroup) {
        GroupService.currentGroup = currentGroup;
    }
}
