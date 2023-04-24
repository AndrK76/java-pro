package ru.otus.config;

public class ConfigurationTooManyComponentException extends RuntimeException{

    private final Class<?> clazz;

    public ConfigurationTooManyComponentException(Class<?> clazz) {
        super("Найдено более одного объекта класса " + clazz.getSimpleName());
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
