package ru.practicum.shareit.exception;

public class AlreadyFieldExistsException extends RuntimeException {
    public AlreadyFieldExistsException(String message) {
        super(message);
    }
}
