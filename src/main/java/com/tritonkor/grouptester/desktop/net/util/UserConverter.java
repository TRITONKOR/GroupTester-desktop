package com.tritonkor.grouptester.desktop.net.util;

import com.tritonkor.grouptester.desktop.net.response.UserResponse;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import java.util.UUID;

public class UserConverter {
    public static User toUser(UserResponse userResponse) {
        User user = new User();
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setBirthday(userResponse.getBirthday());
        user.setAvatar(userResponse.getAvatar());
        user.setRole(userResponse.getRole());
        return user;
    }
}
