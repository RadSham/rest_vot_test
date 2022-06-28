package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.repository.UserRepository;
import ru.javaops.rest_vot_test.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class VoteService {
    private static final LocalTime VOTE_TIME_BORDER = LocalTime.of(11, 0);
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public static LocalDate actualVoteDate() {
        return LocalTime.now().isBefore(VOTE_TIME_BORDER) ? LocalDate.now() : LocalDate.now().plusDays(1);
    }

    public Vote save(Vote vote, int userId) {
        vote.setUser(userRepository.getById(userId));
        vote.setDate(actualVoteDate());
        return voteRepository.save(vote);
    }

    public Vote checkBelong(int id, int userId) {
        return voteRepository.getByIdAndUser(id, userId).orElseThrow(
                () -> new IllegalArgumentException("Vote id=" + id + " doesn't belong to User id=" + userId));
    }

}
