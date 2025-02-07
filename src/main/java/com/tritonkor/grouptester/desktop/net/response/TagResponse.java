package com.tritonkor.grouptester.desktop.net.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private String id;
    private String name;
}
