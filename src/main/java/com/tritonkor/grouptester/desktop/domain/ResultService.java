package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.ResultController;
import com.tritonkor.grouptester.desktop.net.request.result.SaveResultRequest;
import com.tritonkor.grouptester.desktop.net.response.ResultResponse;
import com.tritonkor.grouptester.desktop.persistence.entity.Result;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Service class for managing results in the GroupTester desktop application.
 */
@Service
public class ResultService extends com.tritonkor.grouptester.desktop.domain.Service {
    private static List<ResultResponse> results;

    /**
     * Retrieves results by student based on the specified filters.
     *
     * @param filters The filters to apply.
     * @return The HTTP response containing the results.
     */
    public static HttpResponse<String> getResultsByStudent(Map<String, String> filters) {
        return ResultController.getAllResultsByStudent(filters);
    }

    /**
     * Retrieves results by teacher based on the specified filters.
     *
     * @param filters The filters to apply.
     * @return The HTTP response containing the results.
     */
    public static HttpResponse<String> getResultsByTeacher(Map<String, String> filters) {
        return ResultController.getAllResultsByTeacher(filters);
    }

    /**
     * Saves a result.
     *
     * @param request The request containing the result data.
     * @return The HTTP response indicating the result of the save operation.
     */
    public static HttpResponse<String> saveResult(SaveResultRequest request) {
        return ResultController.saveResult(requestToJson(request));
    }

    /**
     * Retrieves the list of results.
     *
     * @return The list of results.
     */
    public static List<ResultResponse> getResults() {
        return results;
    }

    /**
     * Sets the list of results.
     *
     * @param results The list of results to set.
     */
    public static void setResults(
            List<ResultResponse> results) {
        ResultService.results = results;
    }
}
