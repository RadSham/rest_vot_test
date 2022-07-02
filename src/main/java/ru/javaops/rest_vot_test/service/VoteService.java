package ru.javaops.rest_vot_test.service;

import org.springframework.stereotype.Service;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.error.NotFoundException;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;
import ru.javaops.rest_vot_test.repository.UserRepository;
import ru.javaops.rest_vot_test.repository.VoteRepository;
import ru.javaops.rest_vot_test.web.GlobalExceptionHandler;

import java.time.Clock;
import java.time.LocalTime;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote save(Vote vote, int restaurantId) {
        vote.setRestaurant(restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found Restaurant with id=" + restaurantId)));
        return voteRepository.save(vote);
    }

}
