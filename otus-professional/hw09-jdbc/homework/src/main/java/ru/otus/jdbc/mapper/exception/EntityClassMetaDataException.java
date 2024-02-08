package ru.otus.jdbc.mapper.exception;

public class EntityClassMetaDataException extends RuntimeException {
    public EntityClassMetaDataException() {
    }

    public EntityClassMetaDataException(String message) {
        super(message);
    }

    public EntityClassMetaDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
