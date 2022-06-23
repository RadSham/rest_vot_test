package ru.javaops.restaurant_voting.web.restaurant;


import org.jsoup.helper.HttpConnection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.repository.RestaurantRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class BaseRestaurantController {
    protected final Logger log = getLogger(getClass());

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
