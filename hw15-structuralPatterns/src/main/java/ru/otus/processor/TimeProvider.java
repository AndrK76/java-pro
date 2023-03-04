package ru.otus.processor;

import java.time.LocalDateTime;

@FunctionalInterface
public interface TimeProvider {
    LocalDateTime now();
}
