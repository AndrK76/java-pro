package ru.otus.config;

public class ConfigurationBuildException extends RuntimeException{
    public ConfigurationBuildException(Throwable cause) {
        super(cause);
    }

    public ConfigurationBuildException(String message) {
        super(message);
    }
}
