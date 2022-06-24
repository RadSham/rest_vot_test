package ru.javaops.rest_vot_test.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.User;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
    Optional<User> getByEmail(String email);
}