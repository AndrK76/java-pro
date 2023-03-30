package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;
import ru.otus.samples.SampleEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestEntitySQLMetaData {

    static EntityClassMetaData<?> mockEntity()
    {
        //TODO: Не сообразил как замокать Field
        EntityClassMetaData<?> sampleMetadata = new EntityClassMetaDataImpl<>(SampleEntity.class);
        var metaData = mock(EntityClassMetaData.class);
        when(metaData.getName()).thenReturn("sample");
        when(metaData.getIdField()).thenReturn(sampleMetadata.getIdField());
        when(metaData.getFieldsWithoutId()).thenReturn(sampleMetadata.getFieldsWithoutId());
        when(metaData.getAllFields()).thenReturn(sampleMetadata.getAllFields());

        return metaData;
    }

    @Test
    @DisplayName("Проверка получения selectAll")
    void getSelectAllSqlTest() {
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl(mockEntity());
        var result =  sqlMetaData.getSelectAllSql();
        assertThat(result).isEqualToIgnoringCase("select id, field1, field2 from sample");
    }

    @Test
    @DisplayName("Проверка получения SelectById")
    void getSelectByIdSqlTest() {
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl(mockEntity());
        var select = sqlMetaData.getSelectByIdSql();
        assertThat(select).isEqualToIgnoringCase("select id, field1, field2 from sample where id = ?");
    }

    @Test
    @DisplayName("Проверка получения InsertSql")
    void getInsertSqlTest() {
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl(mockEntity());
        var insert = sqlMetaData.getInsertSql();
        assertThat(insert).isEqualToIgnoringCase("insert into sample (field1, field2) values (?, ?)");
    }

    @Test
    @DisplayName("Проверка получения UpdateSql")
    void getUpdateSqlTest() {
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl(mockEntity());
        var update = sqlMetaData.getUpdateSql();
        assertThat(update).isEqualToIgnoringCase("update sample set field1 = ?, field2 = ? where id = ?");
    }

}
