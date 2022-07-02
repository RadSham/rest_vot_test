package ru.javaops.rest_vot_test.repository;


import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.Restaurant;


@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
