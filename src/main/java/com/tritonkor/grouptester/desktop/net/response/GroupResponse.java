package com.tritonkor.grouptester.desktop.net.response;

import com.tritonkor.grouptester.desktop.persistence.entity.Mark;
import com.tritonkor.grouptester.desktop.persistence.entity.Result;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private String id;
    private String name;
    private String code;
    private Boolean readyToTesting;
    private TestResponse test;
    private Map<UserResponse, Boolean> users;
    private Map<UserResponse, Mark> results;
}
