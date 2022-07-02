package ru.javaops.rest_vot_test.util.validation;

import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.javaops.rest_vot_test.HasId;
import ru.javaops.rest_vot_test.error.IllegalRequestDataException;
import ru.javaops.rest_vot_test.web.GlobalExceptionHandler;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class ValidationUtil {

    public static final LocalTime VOTE_TIME_BORDER = LocalTime.of(11, 0);


    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static void checkDate(LocalDate expected, LocalDate actual) {
        if (!expected.isEqual(actual)) {
            throw new IllegalRequestDataException("Expected date " + expected + " but was " + actual);
        }
    }

    public static void checkTime(Clock clock) {
        if (!LocalTime.now(clock).isBefore(VOTE_TIME_BORDER)) {
            throw new IllegalRequestDataException(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING + " before " + VOTE_TIME_BORDER);
        }
    }

}