package ru.otus.andrk.testlogging;


public class LoggerImpl implements Logger {

    private StringBuilder log = new StringBuilder();

    @Override
    public void add(String msg) {
        log.append(String.format("%s%n", msg));

    }

    @Override
    public String show() {
        return log.toString();
    }

    @Override
    public String toString() {
        return "LoggerImpl{}";
    }
}
