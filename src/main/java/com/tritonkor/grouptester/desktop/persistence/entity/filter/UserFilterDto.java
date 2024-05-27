package com.tritonkor.grouptester.desktop.persistence.entity.filter;

import com.tritonkor.grouptester.desktop.persistence.entity.User;
import java.time.LocalDate;

public record UserFilterDto(
        String username,
        String email,
        LocalDate birthdayFrom,
        LocalDate birthdayTo,
        User.Role role) {

}
