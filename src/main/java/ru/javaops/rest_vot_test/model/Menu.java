package ru.javaops.rest_vot_test.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;


@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "restaurant_id"}, name = "menu_restaurant_idx")})
@Setter
@Getter
public class Menu extends NamedEntity{

    @Column(name = "registered", nullable = false)
    private LocalDate registered;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    //@JsonBackReference
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "menu_dish",
            joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id", referencedColumnName = "id")
    )
    private Set<Dish> dishes;

    public Menu() {
    }

    public Menu(String name, LocalDate registered, Restaurant restaurant) {
        this(null, name, registered, restaurant, null);
    }

    public Menu(String name, LocalDate registered, Restaurant restaurant, Dish... dishes) {
        this(null, name, registered, restaurant, Arrays.asList(dishes));

    }

    public Menu(Integer id, String name, LocalDate registered, Restaurant restaurant) {
        this(id, name, registered, restaurant, null);
    }

    public Menu(Integer id, String name, LocalDate registered, Restaurant restaurant, Collection<Dish> dishes) {
        super(id, name);
        this.registered = registered;
        this.restaurant = restaurant;
        setDishes(dishes);
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Collection<Dish> dishes) {
        this.dishes = CollectionUtils.isEmpty(dishes) ? Collections.emptySet() : Set.copyOf(dishes);
    }

    public void addDishes(Dish... dishes) {
        this.dishes.addAll(Arrays.asList(dishes));
    }

    public void removeDishes(Dish... dishes) {
        Arrays.asList(dishes).forEach(this.dishes::remove);
    }

    public void clearDishes() {
        dishes.clear();
    }

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }
}
