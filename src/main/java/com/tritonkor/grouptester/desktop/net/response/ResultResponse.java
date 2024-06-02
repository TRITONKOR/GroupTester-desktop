package com.tritonkor.grouptester.desktop.net.response;

import com.tritonkor.grouptester.desktop.persistence.entity.Mark;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {
    private UUID id;
    private TestResponse test;
    private UserResponse user;
    private String groupCode;
    private Mark mark;
    private LocalDateTime create_date;
}
