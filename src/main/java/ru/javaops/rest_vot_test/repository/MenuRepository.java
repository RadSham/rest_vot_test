package ru.javaops.rest_vot_test.repository;

import ru.javaops.rest_vot_test.model.Menu;

import java.util.List;

public interface MenuRepository extends BaseRepository{
    List<Menu> getByRestaurantId(int restaurantId);

}
