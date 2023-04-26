package ru.otus.appcontainer.exceptions;

public class ConfigurationBuildException extends RuntimeException{
    public ConfigurationBuildException(Throwable cause) {
        super(cause);
    }

    public ConfigurationBuildException(String message) {
        super(message);
    }
}
