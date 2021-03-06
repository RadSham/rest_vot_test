package ru.javaops.rest_vot_test.web.controller.menuItem;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.rest_vot_test.to.MenuItemTo;
import ru.javaops.rest_vot_test.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuItemController extends BaseMenuItemController {
    public static final String REST_URL = "/api/menu-items";

    @GetMapping
    @Operation(summary = "Get all by filter (default - for current date, all restaurants)", tags = "menu-items")
    public List<MenuItemTo> getByFilter(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                        @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                        @RequestParam @Nullable Integer restaurantId) {
        log.info("getByFilter: restaurant {}, dates({} - {})", restaurantId, startDate, endDate);
        if (startDate == null && endDate == null) {
            LocalDate currentDate = DateTimeUtil.currentDate();
            startDate = currentDate;
            endDate = currentDate;;
        }
        return repository.getByFilter(startDate, endDate, restaurantId);
    }
}
