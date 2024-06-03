package com.tritonkor.grouptester.desktop.presentation.controller.test;

import com.tritonkor.grouptester.desktop.domain.AuthorizeService;
import com.tritonkor.grouptester.desktop.domain.GroupService;
import com.tritonkor.grouptester.desktop.domain.ResultService;
import com.tritonkor.grouptester.desktop.domain.TestService;
import com.tritonkor.grouptester.desktop.net.request.group.LeaveGroupRequest;
import com.tritonkor.grouptester.desktop.net.request.result.SaveResultRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.Answer;
import com.tritonkor.grouptester.desktop.persistence.entity.Mark;
import com.tritonkor.grouptester.desktop.persistence.entity.Question;
import com.tritonkor.grouptester.desktop.presentation.controller.MainController;
import jakarta.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller responsible for managing the process of running a test.
 */
@Component
public class RunTestController {
    @Autowired
    private MainController mainController;

    @FXML
    private Label questionLabel;
    @FXML
    private Label questionCountLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private ImageView photoImageView;

    @FXML
    private Label answerLabel1;
    @FXML
    private Label answerLabel2;
    @FXML
    private Label answerLabel3;
    @FXML
    private Label answerLabel4;

    private CountDownLatch latch;
    private IntegerProperty timeRemaining = new SimpleIntegerProperty();
    private List<Answer> userAnswers;
    private Thread timerThread;
    private Thread testThread;

    private Task<Void> testTask;
    private Task<Void> timerTask;

    /**
     * Initializes the RunTestController after its root element has been completely processed.
     * Starts the test and timer threads.
     */
    @FXML
    public void initialize() {
        userAnswers = new ArrayList<>();

        testTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                runTest();
                return null;
            }
        };
        timerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                runTimer();
                return null;
            }
        };

        timerThread = new Thread(testTask);

        testThread = new Thread(timerTask);
        testThread.start();
    }

    /**
     * Runs the test by iterating through questions and presenting them to the user.
     */
    public void runTest() {
        List<Question> questions = TestService.getCurrentTest().getQuestions();

        int questionsCount = questions.size();
        int currQuestionCount;

        for (Question question : questions) {
            currQuestionCount = questions.indexOf(question) + 1;

            int finalCurrQuestionCount = currQuestionCount;
            Platform.runLater(() -> questionCountLabel.setText(
                    finalCurrQuestionCount + " з " + questionsCount + " питань"));

            List<Answer> answers = question.getAnswers();
            latch = new CountDownLatch(1);

            Platform.runLater(() -> {
                writeQuestionData(question);

                setupLabelClickHandler(answerLabel1, userAnswers, answers.get(0));
                setupLabelClickHandler(answerLabel2, userAnswers, answers.get(1));
                setupLabelClickHandler(answerLabel3, userAnswers, answers.get(2));
                setupLabelClickHandler(answerLabel4, userAnswers, answers.get(3));
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                return;
            }

            if (timeRemaining.get() <= 0) {
                break;
            }
        }

        calculateResult(userAnswers, questionsCount);
    }

    /**
     * Writes the data of the current question to the UI.
     *
     * @param question The current question.
     */
    public void writeQuestionData(Question question) {
        List<Answer> answers = question.getAnswers();

        questionLabel.setText(question.getText());

        byte[] image = question.getImage();
        if (image != null) {
            InputStream inputStream = new ByteArrayInputStream(image);
            photoImageView.setImage(new Image(inputStream));
        } else {
            photoImageView.setImage(null);
        }

        answerLabel1.setText(answers.get(0).getText());
        answerLabel2.setText(answers.get(1).getText());
        answerLabel3.setText(answers.get(2).getText());
        answerLabel4.setText(answers.get(3).getText());
    }

    /**
     * Sets up click handlers for answer labels.
     *
     * @param label The answer label.
     * @param userAnswers The list of user answers.
     * @param answer The answer.
     */
    private void setupLabelClickHandler(Label label, List<Answer> userAnswers, Answer answer) {
        label.setOnMouseClicked(event -> {
            userAnswers.add(answer);
            latch.countDown();
        });
    }

    /**
     * Runs the timer to keep track of the test duration.
     */
    private void runTimer() {
        int testDurationMinutes = TestService.getCurrentTest().getTime();
        int testDurationSeconds = testDurationMinutes * 60;
        timeRemaining.set(testDurationSeconds);

        timerThread.start();

        while (timeRemaining.get() > 0) {
            Platform.runLater(() -> timerLabel.setText(formatTime(timeRemaining.get())));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            timeRemaining.set(timeRemaining.get() - 1);
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Час вийшов");
            alert.setHeaderText(null);
            alert.setContentText("Час тесту вийшов. Результат буде збережено.");
            alert.showAndWait();

            calculateResult(userAnswers, TestService.getCurrentTest().getQuestions().size());
        });
    }

    /**
     * Formats time in seconds to a human-readable format.
     *
     * @param seconds The time in seconds.
     * @return The formatted time string (MM:SS).
     */
    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    /**
     * Calculates the result of the test.
     *
     * @param userAnswers The list of user answers.
     * @param questionsCount The total number of questions.
     */
    private void calculateResult(List<Answer> userAnswers, int questionsCount) {
        closeThreads();

        int correctAnswers = 0;
        for (Answer answer : userAnswers) {
            if (answer.getIsCorrect()) {
                correctAnswers++;
            }
        }
        int score = (int) ((double) correctAnswers / questionsCount * 100);

        Platform.runLater(() -> {
            SaveResultRequest saveRequest = SaveResultRequest.builder()
                    .testId(TestService.getCurrentTest().getId())
                    .userId(AuthorizeService.getCurrentUser().getId())
                    .groupCode(GroupService.getCurrentGroup().getCode())
                    .mark(new Mark(score))
                    .build();

            ResultService.saveResult(saveRequest);

            LeaveGroupRequest leaveGroupRequest = LeaveGroupRequest.builder()
                    .code(GroupService.getCurrentGroup().getCode())
                    .userId(AuthorizeService.getCurrentUser().getId())
                    .build();

            GroupService.leaveGroup(leaveGroupRequest);

            userAnswers.clear();

            String fxml = "view/main_student.fxml";
            mainController.setPage(fxml, timerLabel.getScene());
        });
    }

    @PreDestroy
    private void closeThreads() {
        if (testThread != null && timerThread != null) {
            testThread.interrupt();
            timerThread.interrupt();
        }
    }
}
