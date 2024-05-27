package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.ResultController;
import java.net.http.HttpResponse;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ResultService {
    public static HttpResponse<String> getResultsByUser(Map<String, String> filters) {
        return ResultController.getAllResultsByUser(filters);
    }
}
