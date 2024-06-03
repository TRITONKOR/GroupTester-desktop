package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.CHANGE_USER_STATUS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.CHOOSE_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.CREATE_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.DELETE_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.GROUP_STATUS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.JOIN_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.LEAVE_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.RUN_TEST_URL;

import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Controller class for handling group-related requests in the GroupTester desktop application.
 */
public class GroupController extends RequestController{
    /**
     * Creates a new group.
     *
     * @param requestAsJson The JSON string containing the request data for creating the group.
     * @return The HTTP response indicating the result of the group creation request.
     */
    public static HttpResponse<String> createGroup(String requestAsJson) {
        return postRequest(CREATE_GROUP_URL ,requestAsJson);
    }

    /**
     * Deletes a group.
     *
     * @param requestAsJson The JSON string containing the request data for deleting the group.
     * @return The HTTP response indicating the result of the group deletion request.
     */
    public static HttpResponse<String> deleteGroup(String requestAsJson) {
        return postRequest(DELETE_GROUP_URL, requestAsJson);
    }

    /**
     * Joins a group.
     *
     * @param requestAsJson The JSON string containing the request data for joining the group.
     * @return The HTTP response indicating the result of the group join request.
     */
    public static HttpResponse<String> joinGroup(String requestAsJson) {
        return postRequest(JOIN_GROUP_URL, requestAsJson);
    }

    /**
     * Leaves a group.
     *
     * @param requestAsJson The JSON string containing the request data for leaving the group.
     * @return The HTTP response indicating the result of the group leave request.
     */
    public static HttpResponse<String> leaveGroup(String requestAsJson) {
        return postRequest(LEAVE_GROUP_URL, requestAsJson);
    }

    /**
     * Retrieves the status of a group.
     *
     * @param filters The map containing query parameters for filtering the group status request.
     * @return The HTTP response containing the result of the group status request.
     */
    public static HttpResponse<String> groupStatus(Map<String, String> filters) {
        return getRequest(GROUP_STATUS_URL, filters);
    }

    /**
     * Chooses a test for a group.
     *
     * @param requestAsJson The JSON string containing the request data for choosing the test.
     * @return The HTTP response indicating the result of the test choice request.
     */
    public static HttpResponse<String> chooseTest(String requestAsJson) {
        return postRequest(CHOOSE_TEST_URL, requestAsJson);
    }

    /**
     * Changes the status of a user in a group.
     *
     * @param requestAsJson The JSON string containing the request data for changing the user status.
     * @return The HTTP response indicating the result of the user status change request.
     */
    public static HttpResponse<String> changeUserStatus(String requestAsJson) {
        return postRequest(CHANGE_USER_STATUS_URL, requestAsJson);
    }

    /**
     * Runs a test for a group.
     *
     * @param requestAsJson The JSON string containing the request data for running the test.
     * @return The HTTP response indicating the result of the test run request.
     */
    public static HttpResponse<String> runTest(String requestAsJson) {
        return postRequest(RUN_TEST_URL, requestAsJson);
    }
}
