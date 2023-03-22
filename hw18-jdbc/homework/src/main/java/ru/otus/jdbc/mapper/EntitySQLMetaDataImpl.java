package ru.otus.jdbc.mapper;

import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> metaInfo;
    private String selectAllSql;
    private String selectByIdSql;
    private String updateSql;

    private String insertSql;


    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.metaInfo = entityClassMetaData;
        init();
    }

    @Override
    public String getSelectAllSql() {
        //Можно было сделать вариант select *, но решил заложиться на порядок колонок
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private void init() {
        //Select
        {
            StringBuilder sb = new StringBuilder("select ");
            sb.append(metaInfo.getIdField().getName());
            metaInfo.getFieldsWithoutId().forEach(s -> sb.append(", ").append(s.getName()));
            sb.append(" from ").append(metaInfo.getName());
            selectAllSql = sb.toString();
            sb.append(" where ").append(metaInfo.getIdField().getName()).append(" = ?");
            selectByIdSql = sb.toString();
        }
        //Insert
        {
            StringBuilder sb = new StringBuilder("insert into ");
            sb.append(metaInfo.getName()).append(" (");
            sb.append(metaInfo
                    .getFieldsWithoutId().stream()
                    .map(r -> r.getName())
                    .collect(Collectors.joining(", ")));
            sb.append(") values (");
            sb.append(metaInfo
                    .getFieldsWithoutId().stream()
                    .map(r -> "?")
                    .collect(Collectors.joining(", ")));
            sb.append(")");
            insertSql = sb.toString();
        }
        //Update
        {
            StringBuilder sb = new StringBuilder("update ");
            sb.append(metaInfo.getName()).append(" set ");
            sb.append(metaInfo.getFieldsWithoutId().stream()
                    .map(r -> {
                        return new StringBuilder().append(r.getName()).append(" = ?").toString();
                    })
                    .collect(Collectors.joining(", ")));
            sb.append(" where ").append(metaInfo.getIdField().getName()).append(" = ?");
            updateSql = sb.toString();
        }


    }
}
