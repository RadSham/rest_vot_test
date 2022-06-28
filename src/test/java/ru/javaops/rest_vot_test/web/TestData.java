package ru.javaops.rest_vot_test.web;

import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.model.Role;
import ru.javaops.rest_vot_test.model.User;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.util.JsonUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestData {
    public static final int EXIST_ID = 1;
    public static final int NOT_FOUND_ID = 100500;

    public static class ForUser {
        public static final MatcherFactory.Matcher<User> USER_MATCHER =
                MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "votes", "password");
        public static final int USER_ID = 1;
        public static final int ADMIN_ID = 2;
        public static final String USER_MAIL = "user@gmail.com";
        public static final String ADMIN_MAIL = "admin@voting.ru";
        public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
        public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);

        public static User getNew() {
            return new User(null, "New", "new@gmail.com", "{noop}newPassword", false, new Date(), Collections.singleton(Role.USER));
        }

        public static User getUpdated() {
            return new User(USER_ID, "UpdateName", USER_MAIL, "{noop}newPassword", false, new Date(), List.of(Role.ADMIN));
        }

        public static String jsonWithPassword(User user, String passw) {
            return JsonUtil.writeAdditionProps(user, "password",passw);
        }
    }

    public static class ForRestaurant {
        public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
                MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus", "votes", "dishes");
        public static final Restaurant restaurant = new Restaurant(1, "Noma");
    }

    public static class ForVote {
        public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
        public static final int ADMIN_VOTE_ID = 2;
        public static final Vote vote = new Vote(LocalDate.of(2020, Month.MAY, 20), ForUser.user, ForRestaurant.restaurant);
    }
}
