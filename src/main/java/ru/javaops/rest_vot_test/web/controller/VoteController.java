package ru.javaops.rest_vot_test.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.rest_vot_test.error.NotFoundException;
import ru.javaops.rest_vot_test.model.Rating;
import ru.javaops.rest_vot_test.model.Restaurant;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.repository.VoteRepository;
import ru.javaops.rest_vot_test.service.VoteService;
import ru.javaops.rest_vot_test.to.VoteTo;
import ru.javaops.rest_vot_test.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.rest_vot_test.util.DateTimeUtil.currentDate;
import static ru.javaops.rest_vot_test.util.DateTimeUtil.getClock;
import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    public static final String REST_URL = "/api/votes";

    private final VoteRepository repository;
    private final VoteService service;

    @GetMapping
    @Operation(summary = "Get votes for authorized user by filter (default - for current date)", tags = "votes")
    public List<VoteTo> getByFilter(@AuthenticationPrincipal AuthUser authUser,
                                    @RequestParam @Nullable Integer restaurantId,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = authUser.id();
        if (startDate == null & endDate == null & restaurantId == null) {
            startDate = currentDate();
            endDate = currentDate();
        }
        log.info("getByFilter: user {}, restaurant {}, dates({} - {})", userId, restaurantId, startDate, endDate);
        return repository.getByFilter(userId, restaurantId, startDate, endDate);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id", tags = "votes")
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.get(authUser.id(), id));
    }

    @GetMapping("/rating")
    @Operation(summary = "Get restaurants rating on date (default - for current date)", tags = "votes")
    public List<Rating> getRating(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            log.info("getRating");
        } else {
            log.info("getRating by date {}", date);
        }
        return repository.getRatingByDate(date);
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        log.info("create vote for user {}, restaurant {}", authUser.id(), restaurantId);
        Vote v = new Vote(currentDate(), authUser.getUser());
        Vote created = service.save(v, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping
    @Operation(summary = "Update vote for authorized user", tags = "votes")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("update vote for user {}, restaurant {}", userId, restaurantId);
        checkTime();
        Vote v = repository.getByUserAndDate(userId, currentDate())
                .orElseThrow(() -> new NotFoundException(String.format("Not found Vote today for User[%s]", authUser.getUser().getEmail())));
        service.save(v, restaurantId);
    }


}
