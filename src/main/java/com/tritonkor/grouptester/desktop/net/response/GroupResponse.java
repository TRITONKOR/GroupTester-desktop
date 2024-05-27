package com.tritonkor.grouptester.desktop.net.response;

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
    private Map<UserResponse, Boolean> users;
}
