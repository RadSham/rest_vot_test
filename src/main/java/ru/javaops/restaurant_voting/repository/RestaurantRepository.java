package ru.javaops.restaurant_voting.repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface RestaurantRepository extends BaseRepository{
}
