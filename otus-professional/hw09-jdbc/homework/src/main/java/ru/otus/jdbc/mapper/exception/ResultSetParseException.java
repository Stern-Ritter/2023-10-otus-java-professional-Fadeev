package ru.otus.jdbc.mapper.exception;

public class ResultSetParseException extends RuntimeException {
    public ResultSetParseException() {
    }

    public ResultSetParseException(String message) {
        super(message);
    }

    public ResultSetParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
