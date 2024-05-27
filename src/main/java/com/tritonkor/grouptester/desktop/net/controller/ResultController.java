package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_USER_RESULTS;

import java.net.http.HttpResponse;
import java.util.Map;

public class ResultController extends RequestController{
    public static HttpResponse<String> getAllResultsByUser(Map<String, String> filters) {
        return getRequest(ALL_USER_RESULTS, filters);
    }
}
