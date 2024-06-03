package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_TAGS_URL;

import java.net.http.HttpResponse;
import java.util.HashMap;

/**
 * Controller class for handling tag-related requests in the GroupTester desktop application.
 */
public class TagController extends RequestController {
    /**
     * Retrieves all tags.
     *
     * @return The HTTP response containing all tags.
     */
    public static HttpResponse<String> getAllTestsByUser() {
        return getRequest(ALL_TAGS_URL, new HashMap<String, String>());
    }
}
