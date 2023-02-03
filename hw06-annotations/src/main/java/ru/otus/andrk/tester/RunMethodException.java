package ru.otus.andrk.tester;

public class RunMethodException extends RuntimeException {
    public RunMethodException(String runningMethod, String message, Throwable cause) {
        super(message, cause);
        this.runningMethod = runningMethod;
    }

    public RunMethodException(String runningMethod, Throwable cause) {
        super(cause);
        this.runningMethod = runningMethod;
    }

    public String getRunningMethod() {
        return runningMethod;
    }

    private String runningMethod;
}
