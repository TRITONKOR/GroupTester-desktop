package com.tritonkor.grouptester.desktop.net.request.group;

import com.tritonkor.grouptester.desktop.net.request.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserStatusRequest implements Request {
    @NotNull
    @NotBlank
    private String groupCode;

    @NotNull
    private UUID userId;
}
