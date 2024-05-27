package com.tritonkor.grouptester.desktop.net.response;

import com.tritonkor.grouptester.desktop.persistence.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private String id;
    private String questionId;
    private String text;
    private Boolean isCorrect;
}
