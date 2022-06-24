package ru.javaops.rest_vot_test.repository;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Repository
public interface RestaurantRepository extends BaseRepository {
}
