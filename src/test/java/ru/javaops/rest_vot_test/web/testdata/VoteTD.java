package ru.javaops.rest_vot_test.web.testdata;

import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.model.User;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.util.DateTimeUtil;
import ru.javaops.rest_vot_test.web.MatcherFactory;

import java.time.*;

import static ru.javaops.rest_vot_test.util.DateTimeUtil.currentDate;
import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.VOTE_TIME_BORDER;
import static ru.javaops.rest_vot_test.web.testdata.RestaurantTD.restaurantMirazur;

public class VoteTD {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final int USER_VOTE_1_ID = 1;
    public static final int USER_VOTE_TODAY_ID = 6;
    public static final int ADMIN_VOTE_ID = 2;
    public static final Vote userVote1 = new Vote(1, LocalDate.of(2020, Month.MAY, 20), UserTD.user, RestaurantTD.restaurantNoma);
    public static final Vote userVote2 = new Vote(2, LocalDate.of(2020, Month.MAY, 20), UserTD.admin, RestaurantTD.restaurantNoma);
    public static final Vote userVote3 = new Vote(3, LocalDate.of(2020, Month.MAY, 20), UserTD.userId3, RestaurantTD.restaurantAsador);
    public static final Vote userVote4 = new Vote(4, LocalDate.of(2020, Month.MAY, 21), UserTD.user, RestaurantTD.restaurantNoma);
    public static final Vote userVote6Today = new Vote(USER_VOTE_TODAY_ID, currentDate(), UserTD.user, restaurantMirazur);

    public static final String ratingTodayJSONString = "[{\"data\":{\"id\":2,\"name\":\"Mirazur\"},\"rating\":2}]";
    public static final String ratingOnDateJSONString = "[{\"data\":{\"id\":1,\"name\":\"Noma\"},\"rating\":2},{\"data\":{\"id\":3,\"name\":\"Asador\"},\"rating\":1}]";

    public static Clock voteBorderClock(boolean before) {
        LocalTime time = VOTE_TIME_BORDER.minusMinutes(before ? 1 : 0);
        Instant instant = LocalDateTime.of(currentDate(), time).toInstant(OffsetDateTime.now().getOffset());
        return Clock.fixed(instant, ZoneId.systemDefault());
    }

    public static Vote getNewVote(User user, Restaurant restaurant) {
        return new Vote(null, currentDate(), user, restaurant);
    }

    public static Vote copy(Vote vote) {
        return new Vote(vote.getId(), vote.getActualDate(), vote.getUser(), vote.getRestaurant());
    }
}
