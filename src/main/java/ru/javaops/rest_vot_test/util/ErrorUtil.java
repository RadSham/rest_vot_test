package ru.javaops.rest_vot_test.util;

import ru.javaops.rest_vot_test.error.NotFoundException;

import java.util.function.Supplier;

public class ErrorUtil {
    public static Supplier<NotFoundException> notFound(Class clazz, int id) {
        return () -> new NotFoundException("Not found " + clazz.getSimpleName() + " with id=" + id);
    }
}
