package com.tritonkor.grouptester.desktop.net.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private UUID id;
    private UUID questionId;
    private String text;
    private Boolean isCorrect;
}
