package com.tritonkor.grouptester.desktop.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code User} class represents a user in the system.
 */
@Getter
@Setter
public class User extends Entity {

    private String username;
    private String password;
    private String email;
    private byte[] avatar;
    private LocalDate birthday;
    private Role role;

    /**
     * Constructs a new {@code User} instance.
     *
     * @param id       The unique identifier for the user.
     * @param username The username of the user.
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @param birthday The birthday of the user.
     * @param role     The role of the user.
     */
    private User(UUID id, String username, String email, String password, byte[] avatar, LocalDate birthday,
            Role role) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.birthday = birthday;
        this.role = role;
    }

    public User() {
        super(null);
    }

    /**
     * Returns a {@code UserBuilderId} instance to start building a {@code User}.
     *
     * @return A {@code UserBuilderId} instance.
     */
    public static UserBuilderId builder() {
        return id -> username -> email -> password -> avatar -> birthday -> role -> () -> new User(id,
                username,
                email, password, avatar, birthday, role);
    }

    /**
     * Interface for the {@code User} builder to set the ID.
     */
    public interface UserBuilderId {

        UserBuilderUsername id(UUID id);
    }

    /**
     * Interface for the {@code User} builder to set the username.
     */
    public interface UserBuilderUsername {

        UserBuilderEmail username(String username);
    }

    /**
     * Interface for the {@code User} builder to set the email.
     */
    public interface UserBuilderEmail {

        UserBuilderPassword email(String email);
    }

    /**
     * Interface for the {@code User} builder to set the password.
     */
    public interface UserBuilderPassword {

        UserBuilderAvatar password(String password);
    }

    public interface UserBuilderAvatar {

        UserBuilderBirthday avatar(byte[] avatar);
    }


    /**
     * Interface for the {@code User} builder to set the birthday.
     */
    public interface UserBuilderBirthday {

        UserBuilderRole birthday(LocalDate birthday);
    }

    /**
     * Interface for the {@code User} builder to set the user role.
     */
    public interface UserBuilderRole {

        UserBuilder role(Role role);
    }

    /**
     * Interface for the final steps of the {@code Result} builder.
     */
    public interface UserBuilder {

        User build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * The {@code Role} enum represents the roles that a user can have.
     */
    public enum Role {

        TEACHER("teacher"),
        STUDENT("student");
        private String name;


        Role(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
