package ru.javaops.rest_vot_test.web.testdata;

import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.web.MatcherFactory;

import java.util.Set;

public class RestaurantTD {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus", "votes", "dishes");
    public static final int RESTAURANT_NOMA_ID = 1;
    public static final Restaurant restaurantNoma = new Restaurant(1, "Noma");
    public static final Restaurant restaurantMirazur = new Restaurant(2, "Mirazur");
    public static final Restaurant restaurantAsador = new Restaurant(3, "Asador");

    public static final Restaurant restaurantNomaWithDishes = new Restaurant(1, "Noma", Set.of(DishTD.dish1, DishTD.dish2, DishTD.dish3));
    public static final Restaurant restaurantMirazurWithDishes = new Restaurant(2, "Mirazur", Set.of(DishTD.dish4, DishTD.dish5, DishTD.dish6));
    public static final Restaurant restaurantAsadorWithDishes = new Restaurant(3, "Asador", Set.of(DishTD.dish7, DishTD.dish8, DishTD.dish9));
}
