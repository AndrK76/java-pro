package ru.otus.andrk.domain;

public class CellFullException extends RuntimeException{
    public CellFullException() {
        super();
    }

    public CellFullException(String message) {
        super(message);
    }

    public CellFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public CellFullException(Throwable cause) {
        super(cause);
    }

    protected CellFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
