package ru.practicum.comon.exception;

public class EntityNoAccessException extends RuntimeException {

    public EntityNoAccessException(String message) {
        super(message);
    }
}
