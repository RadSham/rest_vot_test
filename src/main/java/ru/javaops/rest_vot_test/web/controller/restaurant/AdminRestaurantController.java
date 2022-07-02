package ru.javaops.rest_vot_test.web.controller.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.rest_vot_test.model.Menu;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.repository.MenuRepository;
import ru.javaops.rest_vot_test.service.DishService;

import javax.validation.Valid;

import java.net.URI;

import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController extends BaseRestaurantController {
    public static final String REST_URL = "/api/admin/restaurants";

    private final DishService dishService;

    public AdminRestaurantController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        dishService.removeAllDishesFromMenu(id);
        repository.deleteExisted(id);
    }

}
