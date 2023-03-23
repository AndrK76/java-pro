package ru.otus.jdbc.mapper;

public class ReflectMetadataException extends RuntimeException{
    public ReflectMetadataException(String message) {
        super(message);
    }

    public ReflectMetadataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectMetadataException(Throwable cause) {
        super(cause);
    }
}
