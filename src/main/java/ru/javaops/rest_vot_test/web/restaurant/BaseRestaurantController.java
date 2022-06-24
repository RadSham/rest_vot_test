package ru.javaops.rest_vot_test.web.restaurant;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Slf4j
public abstract class BaseRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    protected List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    protected ResponseEntity<Restaurant> get (int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }
}
