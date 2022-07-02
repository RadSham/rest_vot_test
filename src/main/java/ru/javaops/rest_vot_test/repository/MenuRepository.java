package ru.javaops.rest_vot_test.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.Menu;

import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository{
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId")
    List<Menu> getByRestaurantId(int restaurantId);

}
