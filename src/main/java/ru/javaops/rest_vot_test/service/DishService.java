package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.error.NotFoundException;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.model.Menu;
import ru.javaops.rest_vot_test.model.Restaurant;
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

    public Dish get(int id) {
        return dishRepo.findById(id).orElseThrow(() -> new NotFoundException("Not found Dish with id=" + id));
    }

    public Dish saveFromTo(DishTo to) {
        int restaurant_id = to.getRestaurant_id();
        Restaurant restaurant = restRepo.findById(restaurant_id)
                .orElseThrow(() -> new NotFoundException("Not found Restaurant with id=" + restaurant_id));
        Dish dish = new Dish(to.getId(), to.getName(), to.getPrice(), restaurant);        return dishRepo.save(dish);
    }

    public void removeAllDishesFromMenu(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenu(restaurantId, dishRepo.getAllByRestaurantId(restaurantId).toArray(new Dish[0]));
    }

    public void removeFromMenu(int restaurantId, Dish... dishes) {
        List<Menu> menus = menuRepo.getByRestaurantId(restaurantId);
        menus.forEach(menu -> menu.removeDishes(dishes));
        menuRepo.saveAllAndFlush(menus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        Dish dish = dishRepo.getById(id);
        removeFromMenu(dish.getRestaurant().id(), dish);
        dishRepo.deleteExisted(id);
    }
}
