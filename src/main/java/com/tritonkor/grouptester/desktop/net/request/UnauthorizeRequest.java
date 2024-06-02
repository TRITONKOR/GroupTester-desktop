package com.tritonkor.grouptester.desktop.net.request;

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
public class UnauthorizeRequest implements Request{
    @NotNull
    private UUID userId;
}
