package com.tritonkor.grouptester.desktop.net.request.group;

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
public class GroupStatusRequest implements Request {
    @NotNull
    private UUID groupId;
}
