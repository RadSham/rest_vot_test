package ru.javaops.rest_vot_test.error;

public class NotFoundException extends IllegalRequestDataException{
    public NotFoundException(String msg) {
        super(msg);
    }
}
