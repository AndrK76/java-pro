package ru.otus.jdbc.mapper;

import ru.otus.core.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<?> entityClass;
    private Constructor constructor;

    private Field idField;
    private final List<Field> fieldsWithoutId = new ArrayList<>();
    private final List<Field> allFields = new ArrayList<>();


    public EntityClassMetaDataImpl(Class<?> entityClass) {
        this.entityClass = entityClass;
        parseEntity();
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private void parseEntity() {
        constructor = Arrays.stream(entityClass.getConstructors())
                .filter(s -> s.getParameterCount() == 0)
                .findFirst().get();
        Arrays.stream(entityClass.getFields()).forEach((field) -> {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            } else {
                fieldsWithoutId.add(field);
            }
            allFields.add(field);
        });

    }

}
