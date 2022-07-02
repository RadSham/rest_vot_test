package ru.javaops.rest_vot_test.web.testdata;

import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.model.User;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.service.VoteService;
import ru.javaops.rest_vot_test.web.MatcherFactory;

import java.time.*;

import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.VOTE_TIME_BORDER;

public class VoteTD {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final int USER_VOTE_1_ID = 1;
    public static final int USER_VOTE_TODAY_ID = 5;
    public static final int ADMIN_VOTE_ID = 2;
    public static final Vote userVote1 = new Vote(1, LocalDate.of(2020, Month.MAY, 20), UserTD.user, RestaurantTD.restaurantNoma);
    public static final Vote userVote3 = new Vote(3, LocalDate.of(2020, Month.MAY, 21), UserTD.user, RestaurantTD.restaurantNoma);
    public static final Vote userVote5Today = new Vote(USER_VOTE_TODAY_ID, LocalDate.now(), UserTD.user, RestaurantTD.restaurantMirazur);

    public static Clock voteBorderClock(boolean before) {
        LocalTime time = VOTE_TIME_BORDER.minusMinutes(before ? 1 : 0);
        Instant instant = LocalDateTime.of(LocalDate.now(), time).toInstant(OffsetDateTime.now().getOffset());
        return Clock.fixed(instant, ZoneId.systemDefault());
    }

    public static Vote getNewVote(User user, Restaurant restaurant) {
        return new Vote(null, LocalDate.now(VoteService.getClock()), user, restaurant);
    }
}
