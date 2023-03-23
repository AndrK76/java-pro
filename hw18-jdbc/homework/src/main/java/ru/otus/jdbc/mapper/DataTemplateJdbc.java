package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return deserializeFromResultSet(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List<T> resultList = new ArrayList<>();
            try {
                while (rs.next()) {
                    resultList.add(deserializeFromResultSet(rs));
                }
                return resultList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new DataTemplateException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    serializeEntityToList(entity, false));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    serializeEntityToList(entity, true));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T deserializeFromResultSet(ResultSet rs) {
        try {
            T result = entityClassMetaData.getConstructor().newInstance();
            entityClassMetaData.getAllFields().forEach(r -> {
                try {
                    setFieldValue(result, r, rs.getObject(r.getName()));
                } catch (SQLException e) {
                    throw new DataTemplateException(e);
                }
            });
            return result;
        } catch (ReflectiveOperationException e) {
            throw new DataTemplateException(new ReflectMetadataException("Ошибка при создании объекта", e));
        }
    }

    private void setFieldValue(T result, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(result, value);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(new ReflectMetadataException(e));
        }
    }

    private List<Object> serializeEntityToList(T entity, boolean appendId) {
        var fieldList = entityClassMetaData.getFieldsWithoutId();
        if (appendId) {
            fieldList.add(entityClassMetaData.getIdField());
        }
        return fieldList.stream()
                .map(r -> getFieldValue(entity, r))
                .collect(Collectors.toList());
    }

    private Object getFieldValue(T entity, Field field) {
        field.setAccessible(true);
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(new ReflectMetadataException(e));
        }
    }
}
