package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final static String SELECT_ALL_QUERY_TEMPLATE = "SELECT %s FROM %s;";
    private final static String SELECT_BY_ID_QUERY_TEMPLATE = "SELECT %s FROM %s WHERE %s = ?;";
    private final static String INSERT_QUERY_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
    private final static String UPDATE_QUERY_TEMPLATE = "UPDATE %s SET %s WHERE %s = ?";
    private final static String FIELD_NAME_AND_VALUE_TEMPLATE = "%s = ?";
    private final static String FIELD_VALUE_TEMPLATE = "?";

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format(
                SELECT_ALL_QUERY_TEMPLATE,
                getFieldNames(entityClassMetaData.getAllFields()),
                getTableName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(
                SELECT_BY_ID_QUERY_TEMPLATE,
                getFieldNames(entityClassMetaData.getAllFields()),
                getTableName(),
                getIdFieldName());
    }

    @Override
    public String getInsertSql() {
        return String.format(
                INSERT_QUERY_TEMPLATE,
                getTableName(),
                getFieldNames(entityClassMetaData.getFieldsWithoutId()),
                getFieldsValueTemplate(entityClassMetaData.getFieldsWithoutId().size())
        );
    }

    @Override
    public String getUpdateSql() {
        return String.format(
                UPDATE_QUERY_TEMPLATE,
                getTableName(),
                getFieldsNameAndValueTemplate(entityClassMetaData.getFieldsWithoutId()),
                getIdFieldName()
        );
    }

    private String getTableName() {
        return entityClassMetaData.getName();
    }

    private String getIdFieldName() {
        return entityClassMetaData.getIdField().getName();
    }

    private String getFieldNames(List<Field> fields) {
        return fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    private String getFieldsNameAndValueTemplate(List<Field> fields) {
        return fields.stream()
                .map(field -> String.format(FIELD_NAME_AND_VALUE_TEMPLATE, field.getName()))
                .collect(Collectors.joining(", "));
    }

    private String getFieldsValueTemplate(int fieldCount) {
        return Arrays.stream(new String[fieldCount])
                .map((e) -> FIELD_VALUE_TEMPLATE)
                .collect(Collectors.joining(", "));
    }
}
