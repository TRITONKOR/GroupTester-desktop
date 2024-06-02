package com.tritonkor.grouptester.desktop.net.request.test;

import com.tritonkor.grouptester.desktop.net.request.Request;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTestRequest implements Request {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID testId;
}
