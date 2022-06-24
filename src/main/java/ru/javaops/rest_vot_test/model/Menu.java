package ru.javaops.rest_vot_test.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;


@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "restaurant_id"}, name = "menu_restaurant_idx")})
@Setter
@Getter
public class Menu extends NamedEntity{

    @Column(name = "date", nullable = false)
    @NonNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    public Menu() {
    }

    public Menu(LocalDate date, Restaurant restaurant) {
        this(null, null, date, restaurant, null);
    }

    public Menu(String name, LocalDate date, Restaurant restaurant, Dish... dishes) {
        this(null, name, date, restaurant, Arrays.asList(dishes));

    }

    public Menu(Integer id, String name, LocalDate date, Restaurant restaurant, Collection<Dish> dishes) {
        super(id, name);
        this.date = date;
        this.restaurant = restaurant;
    }


}
