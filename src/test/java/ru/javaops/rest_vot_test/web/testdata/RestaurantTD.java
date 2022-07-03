package ru.javaops.rest_vot_test.web.testdata;

import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.web.MatcherFactory;

import java.util.Set;

public class RestaurantTD {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu", "votes", "dishes");
    public static final int RESTAURANT_NOMA_ID = 1;
    public static final Restaurant restaurantNoma = of(1, "Noma");
    public static final Restaurant restaurantMirazur = of(2, "Mirazur");
    public static final Restaurant restaurantAsador = of(3, "Asador");

    public static Restaurant of(Integer id, String name) {
        return new Restaurant(id, name);
    }

}
