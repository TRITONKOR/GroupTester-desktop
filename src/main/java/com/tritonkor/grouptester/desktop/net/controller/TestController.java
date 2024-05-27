package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_USER_TESTS;

import java.net.http.HttpResponse;
import java.util.Map;

public class TestController extends RequestController{
    public static HttpResponse<String> getAllTestsByUser(Map<String, String> filters) {
        return getRequest(ALL_USER_TESTS, filters);
    }

    public static HttpResponse<String> updateQuestion(String requestAsJson) {
        return postRequest(ALL_USER_TESTS, requestAsJson);
    }
}
