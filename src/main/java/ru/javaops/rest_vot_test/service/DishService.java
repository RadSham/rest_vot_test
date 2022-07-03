package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.error.NotFoundException;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.model.Menu;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.DishRepository;
import ru.javaops.rest_vot_test.repository.MenuRepository;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.to.DishTo;

import java.util.List;
import java.util.Optional;

import static ru.javaops.rest_vot_test.util.ErrorUtil.notFound;

@Service
public class DishService {
    private final DishRepository repository;
    private final RestaurantRepository restRepository;
    private final MenuRepository menuRepository;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository, MenuRepository menuRepository) {
        this.restRepository = restaurantRepository;
        this.repository = dishRepository;
        this.menuRepository = menuRepository;
    }

    public List<Dish> getByRestaurantId(Integer restaurantId) {
        return repository.getByRestaurantId(restaurantId);
    }

    public Optional<Dish> findById(int id) {
        return repository.findById(id);
    }

    public Dish saveFromTo(DishTo to) {
        Dish dish = new Dish(to.getId(), to.getName(), to.getPrice(), restRepository.getById(to.getRestaurant_id()));
        return repository.save(dish);
    }

    public Dish checkBelong(int id, int restaurantId) {
        return repository.get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Dish id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }

    public void removeAllDishesFromMenu(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenu(restaurantId, repository.getByRestaurantId(restaurantId).toArray(new Dish[0]));
    }

    public void removeFromMenu(int restaurantId, Dish... dishes) {
        List<Menu> menus = menuRepository.getByRestaurantIdLoadDishes(restaurantId);
        menus.forEach(menu -> menu.removeDishes(dishes));
        menuRepository.saveAllAndFlush(menus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        Dish dish = repository.getById(id);
        removeFromMenu(dish.getRestaurant().id(), dish);
        repository.deleteExisted(id);
    }
}
