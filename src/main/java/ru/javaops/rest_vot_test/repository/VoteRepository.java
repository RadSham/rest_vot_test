package ru.javaops.rest_vot_test.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.rest_vot_test.model.Rating;
import ru.javaops.rest_vot_test.model.Vote;
import ru.javaops.rest_vot_test.to.VoteTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND (:date IS NULL OR v.actualDate = :date)")
    Optional<Vote> getByUserAndDate(int userId, LocalDate date);

    @Query("SELECT new ru.javaops.rest_vot_test.to.VoteTo(v.id, v.actualDate, v.restaurant.id) FROM Vote v WHERE v.user.id = :userId AND v.id = :id")
    Optional<VoteTo> get(int userId, int id);

    @Query("SELECT new ru.javaops.rest_vot_test.to.VoteTo(v.id, v.actualDate, v.restaurant.id) FROM Vote v WHERE v.user.id = :userId " +
            "AND (:startDate IS NULL OR v.actualDate >= :startDate) AND (:endDate IS NULL OR v.actualDate <= :endDate)" +
            "AND (:restaurantId IS NULL OR v.restaurant.id = :restaurantId) ORDER BY v.actualDate DESC")
    List<VoteTo> getByFilter(Integer userId, Integer restaurantId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT new ru.javaops.rest_vot_test.model.Rating(v.restaurant.id, COUNT(v)) FROM Vote v " +
            "WHERE (:date IS NULL AND v.actualDate = CURRENT_DATE) OR v.actualDate = :date GROUP BY v.restaurant ORDER BY COUNT(v) DESC")
    List<Rating> getRatingByDate(LocalDate date);
}
