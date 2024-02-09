package ru.otus.processor.homework.exceptions;

public class EvenSecondException extends RuntimeException {
    public EvenSecondException() {
    }

    public EvenSecondException(String message) {
        super(message);
    }

    public EvenSecondException(String message, Throwable cause) {
        super(message, cause);
    }
}
