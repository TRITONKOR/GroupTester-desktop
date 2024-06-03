package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_USER_TESTS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.CREATE_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.DELETE_QUESTION_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.DELETE_TEST_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.SAVE_QUESTION_TESTS_URL;

import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Controller class for handling test-related requests in the GroupTester desktop application.
 */
public class TestController extends RequestController {
    /**
     * Retrieves all tests associated with a user.
     *
     * @param filters The map containing query parameters for filtering the tests.
     * @return The HTTP response containing the tests associated with the user.
     */
    public static HttpResponse<String> getAllTestsByUser(Map<String, String> filters) {
        return getRequest(ALL_USER_TESTS_URL, filters);
    }

    /**
     * Saves a question in a test.
     *
     * @param requestAsJson The JSON string containing the request data for saving the question.
     * @return The HTTP response indicating the result of the save question request.
     */
    public static HttpResponse<String> saveQuestion(String requestAsJson) {
        return postRequest(SAVE_QUESTION_TESTS_URL, requestAsJson);
    }

    /**
     * Deletes a question from a test.
     *
     * @param requestAsJson The JSON string containing the request data for deleting the question.
     * @return The HTTP response indicating the result of the delete question request.
     */
    public static HttpResponse<String> deleteQuestion(String requestAsJson) {
        return postRequest(DELETE_QUESTION_TEST_URL, requestAsJson);
    }

    /**
     * Deletes a test.
     *
     * @param requestAsJson The JSON string containing the request data for deleting the test.
     * @return The HTTP response indicating the result of the delete test request.
     */
    public static HttpResponse<String> deleteTest(String requestAsJson) {
        return postRequest(DELETE_TEST_URL, requestAsJson);
    }

    /**
     * Creates a new test.
     *
     * @param requestAsJson The JSON string containing the request data for creating the test.
     * @return The HTTP response indicating the result of the create test request.
     */
    public static HttpResponse<String> createTest(String requestAsJson) {
        return postRequest(CREATE_TEST_URL, requestAsJson);
    }
}
