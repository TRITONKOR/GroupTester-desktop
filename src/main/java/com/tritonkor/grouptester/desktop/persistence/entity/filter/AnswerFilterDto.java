package com.tritonkor.grouptester.desktop.persistence.entity.filter;

import java.util.UUID;

public record AnswerFilterDto(
        UUID questionId, String text
) {

}
