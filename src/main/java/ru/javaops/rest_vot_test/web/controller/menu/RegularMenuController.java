package ru.javaops.rest_vot_test.web.controller.menu;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javaops.rest_vot_test.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.rest_vot_test.util.DateTimeUtil.getClock;

@RestController
@RequestMapping(value = RegularMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegularMenuController extends BaseMenuController {
    public static final String REST_URL = "/api/menu";

    @GetMapping
    public List<Menu> getAllWithRestaurant(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getAll");
        if (startDate == null & endDate == null) {
            startDate = LocalDate.now(getClock());
            endDate = LocalDate.now(getClock());
        }
        return repository.getAllWithRestaurantBetween(startDate, endDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.get(id));
    }
}
