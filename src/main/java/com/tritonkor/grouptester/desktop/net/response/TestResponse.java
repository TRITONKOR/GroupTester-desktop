package com.tritonkor.grouptester.desktop.net.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse {
    private String id;
    private String title;
    private List<QuestionResponse> questions;
    private int time;
    private List<TagResponse> tags;
    private LocalDateTime createDate;
}
