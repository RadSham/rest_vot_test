package ru.javaops.rest_vot_test.web.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.rest_vot_test.model.Role;
import ru.javaops.rest_vot_test.model.User;
import ru.javaops.rest_vot_test.to.UserTo;
import ru.javaops.rest_vot_test.util.UserUtil;
import ru.javaops.rest_vot_test.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;
import java.util.EnumSet;

import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.rest_vot_test.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RegularUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "users")
// TODO: cache only most requested data!
public class RegularUserController extends BaseUserController {
    static final String REST_URL = "/api/profile";

    @GetMapping
    @Cacheable
    @Operation(summary = "Get authorized user data", tags = "account")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get auth user {}", authUser.id());
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(key = "#authUser.username")
    @Operation(summary = "Delete yourself", tags = "account")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser.id());
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CachePut(key = "#userTo.email")
    @Operation(summary = "Register yourself", tags = "account")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        created.setRoles(EnumSet.of(Role.USER));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CachePut(key = "#authUser.username")
    @Operation(summary = "Update authorized user data", tags = "account")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody @Valid UserTo userTo) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}