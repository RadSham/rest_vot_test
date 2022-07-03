package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.repository.DishRepository;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.to.DishTo;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    private final DishRepository repository;
    private final RestaurantRepository restRepository;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restRepository = restaurantRepository;
        this.repository = dishRepository;
    }

    public List<Dish> getByRestaurantId(Integer restaurantId) {
        return repository.getByRestaurantId(restaurantId);
    }

    public Optional<Dish> findById(int id) {
        return repository.findById(id);
    }

    public Dish saveFromTo(DishTo to) {
        Dish dish = new Dish(to.getId(), to.getName(), to.getPrice(), restRepository.getById(to.getRestaurantId()));
        return repository.save(dish);
    }

    public void checkBelong(int id, int restaurantId) {
        repository.get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Restaurant [id=%s]", id, restaurantId)));
    }

    public void delete(int id) {
        repository.deleteExisted(id);
    }
}
