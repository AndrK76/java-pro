package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.ReflectMetadataException;
import ru.otus.samples.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestEntityClassMetaData {

    @Test
    @DisplayName("Проверка получения id")
    void getIdFieldTest() {
        EntityClassMetaData<SampleEntity> entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntity.class);
        var idField = entityClassMetaData.getIdField();
        assertThat(idField).isNotNull();
        assertThat(idField.getName()).isEqualTo("id");
    }

    @Test
    @DisplayName("Проверка обработки класса без @Id")
    void noIdFieldTest() {
        assertThrows(ReflectMetadataException.class, () -> {var entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntityWithoutId.class);});
    }


    @Test
    @DisplayName("Проверка получения конструктора")
    void getConstructorTest() {
        EntityClassMetaData<SampleEntity> entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntity.class);
        var constructor = entityClassMetaData.getConstructor();
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка обработки класса без конструктора по умолчанию")
    void noDefaultConstructorTest() {
        assertThrows(ReflectMetadataException.class, () -> {var entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntityWithoutConstructor.class);});
    }


    @Test
    @DisplayName("Проверка получения всех столбцов кроме id")
    void getFieldsWithoutIdTest() {
        EntityClassMetaData<SampleEntity> entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntity.class);
        var fields = entityClassMetaData.getFieldsWithoutId();
        assertThat(fields).isNotNull();
        assertThat(fields).hasSize(2);
    }

    @Test
    @DisplayName("Проверка обработки класса без столбцов данных")
    void noDataFieldsTest() {
        assertThrows(ReflectMetadataException.class, () -> {var entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntityWithoutFields.class);});
    }


    @Test
    @DisplayName("Проверка получения всех столбцов")
    void getAllFieldsTest() {
        EntityClassMetaData<SampleEntity> entityClassMetaData = new EntityClassMetaDataImpl<>(SampleEntity.class);
        var fields = entityClassMetaData.getAllFields();
        assertThat(fields).isNotNull();
        assertThat(fields).hasSize(3);
        var idField = entityClassMetaData.getIdField();
        assertThat(fields).contains(idField);
    }
}
