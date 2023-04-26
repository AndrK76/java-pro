package ru.otus.appcontainer.exceptions;

public class ConfigurationNoExistComponentException extends RuntimeException {

    private final Class<?> clazz;

    public ConfigurationNoExistComponentException(Class<?> clazz) {
        super("Не найдено ни одного объекта класса " + clazz.getSimpleName());
        this.clazz = clazz;
    }

    public ConfigurationNoExistComponentException(String className) {
        super("Не найдено ни одного объекта класса " + className);
        clazz = null;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
