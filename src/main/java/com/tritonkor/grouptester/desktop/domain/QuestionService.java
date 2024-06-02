package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private static Question currentQuestion;

    public static Question getCurrentQuestion() {
        return currentQuestion;
    }

    public static void setCurrentQuestion(Question currentQuestion) {
        QuestionService.currentQuestion = currentQuestion;
    }
}
