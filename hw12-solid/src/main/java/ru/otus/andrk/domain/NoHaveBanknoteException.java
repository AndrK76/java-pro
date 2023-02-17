package ru.otus.andrk.domain;

public class NoHaveBanknoteException extends RuntimeException {
    public NoHaveBanknoteException() {
        super();
    }

    public NoHaveBanknoteException(String message) {
        super(message);
    }

    public NoHaveBanknoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoHaveBanknoteException(Throwable cause) {
        super(cause);
    }

    protected NoHaveBanknoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
