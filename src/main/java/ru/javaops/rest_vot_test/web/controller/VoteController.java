package ru.javaops.rest_vot_test.web.controller;

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
public class VoteController {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    public static final String REST_URL = "/api/votes";

    private final VoteRepository repository;
    private final VoteService service;

    public VoteController(VoteRepository repository, VoteService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<Vote> getByFilter(@RequestParam @Nullable Integer user,
                                  @RequestParam @Nullable Integer restaurant,
                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getByFilter: dates({} - {}), user {}, restaurant {}", startDate, endDate);
        return repository.getByFilterLoadUser(user, restaurant, startDate, endDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/my")
    public List<Vote> getMyBetween(@AuthenticationPrincipal AuthUser authUser,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = authUser.id();
        if (startDate == null & endDate == null) {
            startDate = currentDate();
            endDate = currentDate();
        }
        log.info("getBetween dates({} - {}) for user {}", startDate, endDate, userId);
        return repository.getByUserBetween(userId, startDate, endDate);
    }

    @GetMapping("/rating")
    public List<Rating<Restaurant>> getRating(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            log.info("getRating");
        } else {
            log.info("getRating by date {}", date);
        }
        return repository.getRatingBetween(date);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete vote of user {}", authUser.id());
        checkTime();
        Vote vote = repository.getByUserOnDate(authUser.id(), currentDate())
                .orElseThrow(() -> new NotFoundException(String.format("Not found Vote today for User[%s]", authUser.getUser().getEmail())));
        repository.delete(vote);
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurant) {
        log.info("create vote for user {}, restaurant {}", authUser.id(), restaurant);
        checkTime();
        Vote v = new Vote(currentDate(), authUser.getUser());
        Vote created = service.save(v, restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurant) {
        int userId = authUser.id();
        log.info("update vote for user {}, restaurant {}", userId, restaurant);
        checkTime();
        Vote v = repository.getByUserOnDate(userId, currentDate())
                .orElseThrow(() -> new NotFoundException(String.format("Not found Vote today for User[%s]", authUser.getUser().getEmail())));
        service.save(v, restaurant);
    }


}
