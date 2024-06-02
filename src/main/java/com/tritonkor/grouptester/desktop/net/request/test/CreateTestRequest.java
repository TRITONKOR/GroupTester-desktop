package com.tritonkor.grouptester.desktop.net.request.test;

import com.tritonkor.grouptester.desktop.net.request.Request;
import com.tritonkor.grouptester.desktop.persistence.entity.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTestRequest implements Request {
    @NotNull
    private UUID userId;

    @NotNull
    @NotBlank
    private String testTitle;

    @NotNull
    @Min(0)
    private int time;

    @NotNull
    private List<Tag> tags;
}
