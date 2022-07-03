package ru.javaops.rest_vot_test.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.rest_vot_test.model.Dish;
import ru.javaops.rest_vot_test.repository.DishRepository;
import ru.javaops.rest_vot_test.service.DishService;
import ru.javaops.rest_vot_test.to.DishTo;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController  {
    public static final String REST_URL = "/api/admin/dishes";

    private final DishService service;

    @GetMapping
    @Operation(summary = "Get all by restaurant Id (default - all restaurants)", tags = "dishes")
    public List<Dish> getByFilter(@RequestParam @Nullable Integer restaurantId) {
        log.info("getByFilter: restaurant {}", restaurantId);
        return service.getByRestaurantId(restaurantId);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get by id", tags = "dishes")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(service.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create", tags = "dishes")
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo to) {
        log.info("create from TO{}", to);
        checkNew(to);
        Dish created = service.saveFromTo(to);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update", tags = "dishes")
    public void update(@Valid @RequestBody DishTo to, @PathVariable int id) {
        log.info("update from TO {}", to);
        assureIdConsistent(to, id);
        service.checkBelong(id, to.getRestaurant_id());
        service.saveFromTo(to);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete", tags = "dishes")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }
}
