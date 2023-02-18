package ru.otus.andrk.domain;

public class BanknoteMissmatchException extends RuntimeException{
    public BanknoteMissmatchException() {
        super();
    }

    public BanknoteMissmatchException(String message) {
        super(message);
    }

    public BanknoteMissmatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public BanknoteMissmatchException(Throwable cause) {
        super(cause);
    }

    protected BanknoteMissmatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
