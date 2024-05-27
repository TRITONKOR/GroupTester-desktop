package com.tritonkor.grouptester.desktop.net.response;

import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import java.time.LocalDate;
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
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private byte[] avatar;
    private LocalDate birthday;
    private Role role;
}
