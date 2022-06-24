package ru.javaops.rest_vot_test.to;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class RestaurantToDelete extends NamedTo implements Serializable {

    @Serial
    private static final long serialVersionID = 5581350404739469341L;

    private long rating;

    public RestaurantToDelete() {
    }

    public RestaurantToDelete(Integer id, String name, long rating) {
        super(id, name);
        this.rating = rating;
    }
}
