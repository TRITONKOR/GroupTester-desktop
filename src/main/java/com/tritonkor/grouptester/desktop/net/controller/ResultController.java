package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_STUDENT_RESULTS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.ALL_TEACHER_RESULTS_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.SAVE_RESULT_URL;

import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Controller class for handling result-related requests in the GroupTester desktop application.
 */
public class ResultController extends RequestController{
    /**
     * Retrieves all results for a student.
     *
     * @param filters The map containing query parameters for filtering the results.
     * @return The HTTP response containing the results for the student.
     */
    public static HttpResponse<String> getAllResultsByStudent(Map<String, String> filters) {
        return getRequest(ALL_STUDENT_RESULTS_URL, filters);
    }

    /**
     * Retrieves all results for a teacher.
     *
     * @param filters The map containing query parameters for filtering the results.
     * @return The HTTP response containing the results for the teacher.
     */
    public static HttpResponse<String> getAllResultsByTeacher(Map<String, String> filters) {
        return getRequest(ALL_TEACHER_RESULTS_URL, filters);
    }

    /**
     * Saves a result.
     *
     * @param requestAsJson The JSON string containing the request data for saving the result.
     * @return The HTTP response indicating the result of the save result request.
     */
    public static HttpResponse<String> saveResult(String requestAsJsom) {
        return postRequest(SAVE_RESULT_URL, requestAsJsom);
    }
}
