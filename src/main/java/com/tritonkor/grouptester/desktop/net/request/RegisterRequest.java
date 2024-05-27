package com.tritonkor.grouptester.desktop.net.request;

import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private LocalDate birthday;
    private byte[] avatar;
    private Role role;
}
