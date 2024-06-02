package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_TAGS_URL;

import java.net.http.HttpResponse;
import java.util.HashMap;

public class TagController extends RequestController{
    public static HttpResponse<String> getAllTestsByUser() {
        return getRequest(ALL_TAGS_URL, new HashMap<String, String>());
    }
}
