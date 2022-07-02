package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.model.Menu;
import ru.javaops.rest_vot_test.repository.DishRepository;
import ru.javaops.rest_vot_test.repository.MenuRepository;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.to.DishTo;

import java.util.List;

@Service
public class DishService {
    private final RestaurantRepository restRepo;
    private final DishRepository dishRepo;
    private final MenuRepository menuRepo;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository, MenuRepository menuRepository) {
        this.restRepo = restaurantRepository;
        this.dishRepo = dishRepository;
        this.menuRepo = menuRepository;
    }

    public Dish saveFromTo(DishTo to) {
        return dishRepo.save( new Dish(to.getId(), to.getName(), to.getPrice(), restRepo.getById(to.getRestaurant_id())) );
    }

    public void removeAllDishesFromMenus(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenus(restaurantId, dishRepo.getAllByRestaurantId(restaurantId).toArray(new Dish[0]));
    }

    public void removeFromMenus(int restaurantId, Dish... dishes) {
        List<Menu> menus = menuRepo.getByRestaurantId(restaurantId);
        menus.forEach(menu -> menu.removeDishes(dishes));
        menuRepo.saveAllAndFlush(menus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        Dish dish = dishRepo.getById(id);
        removeFromMenus(dish.getRestaurant().id(), dish);
        dishRepo.deleteExisted(id);
    }
}
