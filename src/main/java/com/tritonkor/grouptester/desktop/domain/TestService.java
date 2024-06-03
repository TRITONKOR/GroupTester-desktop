package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.TestController;
import com.tritonkor.grouptester.desktop.net.request.question.DeleteQuestionRequest;
import com.tritonkor.grouptester.desktop.net.request.question.SaveQuestionRequest;
import com.tritonkor.grouptester.desktop.net.request.test.CreateTestRequest;
import com.tritonkor.grouptester.desktop.net.request.test.DeleteTestRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Test;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

/**
 * Service class for managing tests in the GroupTester desktop application.
 */
@Service
public class TestService extends com.tritonkor.grouptester.desktop.domain.Service{
    private static Test currentTest;

    /**
     * Saves a question.
     *
     * @param request The request containing the question data.
     * @return The HTTP response indicating the result of the save operation.
     */
    public static HttpResponse<String> saveQuestion(SaveQuestionRequest request) {
        return TestController.saveQuestion(requestToJson(request));
    }

    /**
     * Creates a new test.
     *
     * @param request The request containing the test data.
     * @return The HTTP response indicating the result of the create operation.
     */
    public static HttpResponse<String> createTest(CreateTestRequest request) {
        return TestController.createTest(requestToJson(request));
    }

    /**
     * Deletes a question.
     *
     * @param request The request containing the question data.
     * @return The HTTP response indicating the result of the delete operation.
     */
    public static HttpResponse<String> deleteQuestion(DeleteQuestionRequest request) {
        return TestController.deleteQuestion(requestToJson(request));
    }

    /**
     * Deletes a test.
     *
     * @param request The request containing the test data.
     * @return The HTTP response indicating the result of the delete operation.
     */
    public static HttpResponse<String> deleteTest(DeleteTestRequest request) {
        return TestController.deleteTest(requestToJson(request));
    }

    /**
     * Retrieves the currently selected test.
     *
     * @return The currently selected test.
     */
    public static Test getCurrentTest() {
        return currentTest;
    }

    /**
     * Sets the currently selected test.
     *
     * @param currentTest The test to set as the currently selected test.
     */
    public static void setCurrentTest(Test currentTest) {
        TestService.currentTest = currentTest;
    }
}
