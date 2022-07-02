package ru.javaops.rest_vot_test.web;

import org.hibernate.persister.entity.SingleTableEntityPersister;
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
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.repository.VoteRepository;
import ru.javaops.rest_vot_test.service.VoteService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.rest_vot_test.service.VoteService.getClock;
import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api/votes";

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
        return repository.getByFilter(user, restaurant, startDate, endDate);
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
        log.info("getBetween dates({} - {}) for user {}", startDate, endDate, userId);
        return repository.getBetween(userId, startDate, endDate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Vote vote = service.checkBelong(id, authUser.id());
        checkDate(LocalDate.now(getClock()), vote.getDate());
        repository.delete(vote);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote) {
        log.info("create {}", vote);
        service.checkTime();
        checkNew(vote);
        Vote created = service.save(vote,authUser.id());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "{/id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update (@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", vote, userId);
        service.checkTime();
        checkDate(LocalDate.now(getClock()), vote.getDate());
        assureIdConsistent(vote,id);
        service.checkBelong(id, userId);
        service.save(vote, userId);
    }


}
