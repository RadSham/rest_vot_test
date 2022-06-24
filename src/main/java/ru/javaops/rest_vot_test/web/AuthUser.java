package ru.javaops.rest_vot_test.web;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import ru.javaops.rest_vot_test.model.User;

@Getter
@ToString(of = "user")
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }
}