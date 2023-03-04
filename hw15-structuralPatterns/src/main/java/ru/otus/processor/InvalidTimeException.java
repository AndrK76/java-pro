package ru.otus.processor;

import java.time.LocalDateTime;

public class InvalidTimeException extends RuntimeException{
    public InvalidTimeException(LocalDateTime now) {
        super(now.toString() + ": is invalid time");
    }
}
