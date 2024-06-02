package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_USER_TESTS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.CREATE_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.DELETE_QUESTION_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.DELETE_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.SAVE_QUESTION_TESTS_URL;

import java.net.http.HttpResponse;
import java.util.Map;

public class TestController extends RequestController{
    public static HttpResponse<String> getAllTestsByUser(Map<String, String> filters) {
        return getRequest(ALL_USER_TESTS_URL, filters);
    }

    public static HttpResponse<String> saveQuestion(String requestAsJson) {
        return postRequest(SAVE_QUESTION_TESTS_URL, requestAsJson);
    }

    public static HttpResponse<String> deleteQuestion(String requestAsJson) {
        return postRequest(DELETE_QUESTION_TEST_URL, requestAsJson);
    }

    public static HttpResponse<String> deleteTest(String requestAsJson) {
        return postRequest(DELETE_TEST_URL, requestAsJson);
    }

    public static HttpResponse<String> createTest(String requestAsJson) {
        return postRequest(CREATE_TEST_URL, requestAsJson);
    }
}
