package ru.javaops.rest_vot_test.web.controller.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.rest_vot_test.error.NotFoundException;
import ru.javaops.rest_vot_test.model.Menu;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.MenuRepository;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.service.DishService;
import ru.javaops.rest_vot_test.to.MenuTo;

import java.time.LocalDate;

import static ru.javaops.rest_vot_test.util.DateTimeUtil.currentDate;
import static ru.javaops.rest_vot_test.util.DateTimeUtil.getClock;

public abstract class BaseMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuRepository menuRepo;
    @Autowired
    protected RestaurantRepository restaurantRepo;
    @Autowired
    protected DishService dishService;

    protected Menu fromTo(MenuTo to) {
        Restaurant restaurant = restaurantRepo.findById(to.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Not found Restaurant with id=" + to.getRestaurantId()));
        if (to.getRegistered() == null) {
            to.setRegistered(currentDate());
        }
        return new Menu(to.getId(), to.getName(), to.getRegistered(), restaurant);

    }
}
