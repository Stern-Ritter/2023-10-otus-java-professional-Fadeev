package ru.otus.exceptions;

public class UnsupportedBanknoteDenominationException extends RuntimeException {
    public UnsupportedBanknoteDenominationException() {
    }

    public UnsupportedBanknoteDenominationException(String message) {
        super(message);
    }

    public UnsupportedBanknoteDenominationException(String message, Throwable cause) {
        super(message, cause);
    }
}
