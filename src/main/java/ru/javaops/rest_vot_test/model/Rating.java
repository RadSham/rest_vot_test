package ru.javaops.rest_vot_test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Rating<T>
{
    private T record;
    private long rating;
}
