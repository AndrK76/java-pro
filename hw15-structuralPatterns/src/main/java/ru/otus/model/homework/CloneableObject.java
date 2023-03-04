package ru.otus.model.homework;

@FunctionalInterface
public interface CloneableObject<T> {
    T getClone();
}
