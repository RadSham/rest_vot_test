package ru.javaops.rest_vot_test.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository <Dish> {
    List<Dish> getAllByRestaurantId(int restaurantId);

    Optional<Dish> get(int id, int restaurantId);

}
