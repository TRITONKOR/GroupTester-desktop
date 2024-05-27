package com.tritonkor.grouptester.desktop.persistence.entity.filter;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestFilterDto(
        String title,
        UUID ownerId,
        LocalDateTime createdAtStart,
        LocalDateTime createdAtEnd
) {

}
