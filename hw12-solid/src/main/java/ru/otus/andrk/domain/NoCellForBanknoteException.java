package ru.otus.andrk.domain;

public class NoCellForBanknoteException extends RuntimeException {
    public NoCellForBanknoteException() {
        super();
    }

    public NoCellForBanknoteException(String message) {
        super(message);
    }

    public NoCellForBanknoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCellForBanknoteException(Throwable cause) {
        super(cause);
    }

    protected NoCellForBanknoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
