package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import org.springframework.stereotype.Service;

/**
 * Service class for managing questions in the GroupTester desktop application.
 */
@Service
public class QuestionService {

    private static Question currentQuestion;

    /**
     * Retrieves the current question.
     *
     * @return The current question.
     */
    public static Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Sets the current question.
     *
     * @param currentQuestion The question to set as the current question.
     */
    public static void setCurrentQuestion(Question currentQuestion) {
        QuestionService.currentQuestion = currentQuestion;
    }
}
