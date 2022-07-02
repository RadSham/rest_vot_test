package ru.javaops.rest_vot_test.web.controller.restaurant;



import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.rest_vot_test.repository.RestaurantRepository;

public abstract class BaseRestaurantController {

    @Autowired
    protected RestaurantRepository repository;
}
