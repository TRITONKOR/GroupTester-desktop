package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.net.request.question.SaveQuestionRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class TestService extends com.tritonkor.grouptester.desktop.domain.Service{
    public static HttpResponse<String> updateQuestion(SaveQuestionRequest request) {
        return TestController.updateQuestion(requestToJson(request));
    }
}
