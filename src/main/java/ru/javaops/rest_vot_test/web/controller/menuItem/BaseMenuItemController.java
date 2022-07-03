package ru.javaops.rest_vot_test.web.controller.menuItem;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.model.MenuItem;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.DishRepository;
import ru.javaops.rest_vot_test.repository.MenuItemRepository;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.to.MenuItemTo;

import java.util.Objects;

import static ru.javaops.rest_vot_test.util.DateTimeUtil.currentDate;
import static ru.javaops.rest_vot_test.util.ErrorUtil.notFound;

public abstract class BaseMenuItemController {

    @Autowired
    protected MenuItemRepository repository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    protected MenuItem prepareToSave(MenuItemTo to) {
        Dish dish = dishRepository.findById(to.getDishId())
                .orElseThrow(notFound(Dish.class, to.getDishId()));

        // check belong to restaurant
        Restaurant restaurant = restaurantRepository.findById(to.getRestaurantId())
                .orElseThrow(notFound(Restaurant.class, to.getRestaurantId()));
        if (!Objects.equals(dish.getRestaurant().getId(), restaurant.getId())) {
            throw new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Restaurant [id=%s]", dish.getId(), restaurant.getId()));
        }

        if (to.getActualDate() == null) {
            to.setActualDate(currentDate());
        }
        return new MenuItem(to.getId(), to.getActualDate(), dish);
    }
}
