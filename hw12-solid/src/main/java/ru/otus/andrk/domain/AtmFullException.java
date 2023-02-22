package ru.otus.andrk.domain;

public class AtmFullException extends RuntimeException{
    public AtmFullException() {
        super();
    }

    public AtmFullException(String message) {
        super(message);
    }

    public AtmFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public AtmFullException(Throwable cause) {
        super(cause);
    }

    protected AtmFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
