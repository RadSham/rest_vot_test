package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.repository.UserRepository;
import ru.javaops.rest_vot_test.repository.VoteRepository;
import ru.javaops.rest_vot_test.web.GlobalExceptionHandler;

import java.time.Clock;
import java.time.LocalTime;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public Vote save(Vote vote, int userId) {
        vote.setUser(userRepository.getById(userId));
        return voteRepository.save(vote);
    }

    public Vote checkBelong(int id, int userId) {
        return voteRepository.getByIdAndUser(id, userId).orElseThrow(
                () -> new IllegalArgumentException("Vote id=" + id + " doesn't belong to User id=" + userId));
    }

}
