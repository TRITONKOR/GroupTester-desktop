package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.CREATE_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.DELETE_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.GROUP_STATUS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.JOIN_GROUP_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.LEAVE_GROUP_URL;

import java.net.http.HttpResponse;
import java.util.Map;

public class GroupController extends RequestController{
    public static HttpResponse<String> createGroup(String requestAsJson) {
        return postRequest(CREATE_GROUP_URL ,requestAsJson);
    }

    public static HttpResponse<String> deleteGroup(String requestAsJson) {
        return postRequest(DELETE_GROUP_URL, requestAsJson);
    }

    public static HttpResponse<String> joinGroup(String requestAsJson) {
        return postRequest(JOIN_GROUP_URL, requestAsJson);
    }

    public static HttpResponse<String> leaveGroup(String requestAsJson) {
        return postRequest(LEAVE_GROUP_URL, requestAsJson);
    }

    public static HttpResponse<String> groupStatus(Map<String, String> filters) {
        return getRequest(GROUP_STATUS_URL, filters);
    }
}
