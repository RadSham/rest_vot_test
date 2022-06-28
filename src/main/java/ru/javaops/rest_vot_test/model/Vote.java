package ru.javaops.rest_vot_test.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "user_id"}, name = "vote_date_user_idx")})
@Getter
@Setter
public class Vote extends BaseEntity{

    @Column(name = "date", nullable = false)
    @NonNull
    private LocalDate date;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    //@JsonBackReference
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    //@JsonBackReference

    @NotNull
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(@NonNull LocalDate date, User user, Restaurant restaurant) {
        this.date = date;
        this.user = user;
        this.restaurant = restaurant;
    }
}
