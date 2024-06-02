package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_STUDENT_RESULTS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_TEACHER_RESULTS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.SAVE_RESULT_URL;

import java.net.http.HttpResponse;
import java.util.Map;

public class ResultController extends RequestController{
    public static HttpResponse<String> getAllResultsByStudent(Map<String, String> filters) {
        return getRequest(ALL_STUDENT_RESULTS_URL, filters);
    }

    public static HttpResponse<String> getAllResultsByTeacher(Map<String, String> filters) {
        return getRequest(ALL_TEACHER_RESULTS_URL, filters);
    }

    public static HttpResponse<String> saveResult(String requestAsJsom) {
        return postRequest(SAVE_RESULT_URL, requestAsJsom);
    }
}
