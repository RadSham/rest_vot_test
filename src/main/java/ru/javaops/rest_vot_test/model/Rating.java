package ru.javaops.rest_vot_test.model;

import lombok.*;

@Value
@ToString
public class Rating {
    int id;
    int rating;

    public Rating(int id, long rating) {
        this.id = id;
        this.rating = (int) rating;
    }
}
