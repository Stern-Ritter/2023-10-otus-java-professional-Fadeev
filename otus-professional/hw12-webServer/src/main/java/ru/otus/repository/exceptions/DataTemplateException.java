package ru.otus.repository.exceptions;

public class DataTemplateException extends RuntimeException {
    public DataTemplateException() {
    }

    public DataTemplateException(String message) {
        super(message);
    }

    public DataTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
