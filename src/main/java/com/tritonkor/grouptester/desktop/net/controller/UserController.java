package com.tritonkor.grouptester.desktop.net.controller;


import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_USERS_URL;

import java.net.http.HttpResponse;
import java.util.Map;

public class UserController extends RequestController{

    public static HttpResponse<String> getAllUsers(Map<String, String> filters) {
        return getRequest(ALL_USERS_URL, filters);
    }
}
