package ru.javaops.rest_vot_test.web.controller.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.error.NotFoundException;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.model.Menu;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.MenuRepository;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.service.DishService;
import ru.javaops.rest_vot_test.to.MenuTo;

import java.time.LocalDate;

import static ru.javaops.rest_vot_test.util.DateTimeUtil.currentDate;
import static ru.javaops.rest_vot_test.util.DateTimeUtil.getClock;
import static ru.javaops.rest_vot_test.util.ErrorUtil.notFound;

public abstract class BaseMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuRepository repository;
    @Autowired
    protected RestaurantRepository restaurantRepository;
    @Autowired
    protected DishService dishService;

    protected Menu fromTo(MenuTo to) {
        Restaurant restaurant = restaurantRepository.findById(to.getRestaurantId())
                .orElseThrow(notFound(Restaurant.class, to.getRestaurantId()));
        if (to.getRegistered() == null) {
            to.setRegistered(currentDate());
        }
        return new Menu(to.getId(), to.getName(), to.getRegistered(), restaurant);

    }

    protected Menu getByIdLoad(int id) {
        return repository.getByIdLoad(id).orElseThrow(notFound(Menu.class, id));
    }

    protected static void checkBelongToRestaurant(Menu menu, Dish dish) {
        if (!menu.getRestaurant().equals(dish.getRestaurant())) {
            throw new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Restaurant [id=%s]", dish.getId(), dish.getRestaurant().getId()));
        }
    }

    protected static void checkBelongToMenu(Menu menu, Dish dish) {
        if (!menu.getDishes().contains(dish)) {
            throw new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Menu [id=%s]", dish.getId(), menu.getId()));
        }
    }
}
