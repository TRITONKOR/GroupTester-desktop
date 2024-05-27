package com.tritonkor.grouptester.desktop.net.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private String id;
    private String testId;
    private String text;
    private List<AnswerResponse> answers;
}
