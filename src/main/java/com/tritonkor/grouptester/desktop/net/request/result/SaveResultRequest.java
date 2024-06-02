package com.tritonkor.grouptester.desktop.net.request.result;

import com.tritonkor.grouptester.desktop.net.request.Request;
import com.tritonkor.grouptester.desktop.persistence.entity.Mark;
import jakarta.validation.constraints.NotBlank;
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
public class SaveResultRequest implements Request {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID testId;

    @NotBlank
    private String groupCode;

    @NotNull
    private Mark mark;
}
