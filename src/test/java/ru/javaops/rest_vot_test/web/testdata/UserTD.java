package ru.javaops.rest_vot_test.web.testdata;

import ru.javaops.rest_vot_test.model.Role;
import ru.javaops.rest_vot_test.model.User;
import ru.javaops.rest_vot_test.util.JsonUtil;
import ru.javaops.rest_vot_test.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserTD {
    public static final MatcherFactory.Matcher<User> USER_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "votes", "password");
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final String ADMIN_MAIL = "admin@voting.ru";
    public static final String USER_MAIL = "user@gmail.com";
    public static final String USER2_MAIL = "user2@gmail.com";

    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
    public static final User user2 = new User(3, "User2", "user2@gmail.com", "password2", Role.USER);


    public static User getNewUser() {
        return new User(null, "New", "new@gmail.com", "{noop}newPassword", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "{noop}newPassword", false, new Date(), List.of(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
