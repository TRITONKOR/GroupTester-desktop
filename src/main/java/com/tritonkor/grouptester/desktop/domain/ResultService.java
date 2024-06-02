package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.ResultController;
import com.tritonkor.grouptester.desktop.net.request.result.SaveResultRequest;
import com.tritonkor.grouptester.desktop.net.response.ResultResponse;
import com.tritonkor.grouptester.desktop.persistence.entity.Result;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ResultService extends com.tritonkor.grouptester.desktop.domain.Service {
    private static List<ResultResponse> results;

    public static HttpResponse<String> getResultsByStudent(Map<String, String> filters) {
        return ResultController.getAllResultsByStudent(filters);
    }

    public static HttpResponse<String> getResultsByTeacher(Map<String, String> filters) {
        return ResultController.getAllResultsByTeacher(filters);
    }

    public static HttpResponse<String> saveResult(SaveResultRequest request) {
        return ResultController.saveResult(requestToJson(request));
    }

    public static List<ResultResponse> getResults() {
        return results;
    }

    public static void setResults(
            List<ResultResponse> results) {
        ResultService.results = results;
    }
}
