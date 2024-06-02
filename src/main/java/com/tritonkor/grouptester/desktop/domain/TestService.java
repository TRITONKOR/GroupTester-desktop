package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.net.request.question.DeleteQuestionRequest;
import com.tritonkor.grouptester.desktop.net.request.question.SaveQuestionRequest;
import com.tritonkor.grouptester.desktop.net.request.test.CreateTestRequest;
import com.tritonkor.grouptester.desktop.net.request.test.DeleteTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class TestService extends com.tritonkor.grouptester.desktop.domain.Service{
    private static Test currentTest;
    public static HttpResponse<String> saveQuestion(SaveQuestionRequest request) {
        return TestController.saveQuestion(requestToJson(request));
    }

    public static HttpResponse<String> createTest(CreateTestRequest request) {
        return TestController.createTest(requestToJson(request));
    }

    public static HttpResponse<String> deleteQuestion(DeleteQuestionRequest request) {
        return TestController.deleteQuestion(requestToJson(request));
    }

    public static HttpResponse<String> deleteTest(DeleteTestRequest request) {
        return TestController.deleteTest(requestToJson(request));
    }

    public static Test getCurrentTest() {
        return currentTest;
    }

    public static void setCurrentTest(Test currentTest) {
        TestService.currentTest = currentTest;
    }
}
