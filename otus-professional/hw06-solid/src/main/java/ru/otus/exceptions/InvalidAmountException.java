package ru.otus.exceptions;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
    }

    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
