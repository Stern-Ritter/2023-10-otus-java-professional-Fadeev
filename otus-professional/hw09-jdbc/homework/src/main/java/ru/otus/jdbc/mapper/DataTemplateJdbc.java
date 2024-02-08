package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.exception.ResultSetParseException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final String RESULT_SET_PARSE_EXCEPTION = "Can not parse result set.";

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final Function<ResultSet, T> entityRsHandler;
    private final Function<ResultSet, List<T>> entitiesRsHandler;
    ;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityRsHandler = rs -> {
            try {
                T entity = null;
                if (rs.next()) {
                    entity = parseEntity(rs);
                }
                return entity;
            } catch (ReflectiveOperationException | SQLException e) {
                throw new ResultSetParseException(RESULT_SET_PARSE_EXCEPTION, e);
            }
        };

        this.entitiesRsHandler = rs -> {
            try {
                List<T> entities = new ArrayList<>();
                while (rs.next()) {
                    T entity = parseEntity(rs);
                    entities.add(entity);
                }
                return entities;
            } catch (ReflectiveOperationException | SQLException e) {
                throw new ResultSetParseException(RESULT_SET_PARSE_EXCEPTION, e);
            }
        };
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String query = entitySQLMetaData.getSelectByIdSql();
        List<Object> params = List.of(id);

        return dbExecutor.executeSelect(connection, query, params, entityRsHandler);
    }

    @Override
    public List<T> findAll(Connection connection) {
        String query = entitySQLMetaData.getSelectAllSql();
        List<Object> params = Collections.emptyList();
        List<T> defaultValue = Collections.emptyList();

        return dbExecutor.executeSelect(connection, query, params, entitiesRsHandler).orElse(defaultValue);
    }

    @Override
    public long insert(Connection connection, T client) {
        String query = entitySQLMetaData.getInsertSql();
        List<Object> fieldValues = entityClassMetaData.getFieldValues(client, entityClassMetaData.getFieldsWithoutId());

        return dbExecutor.executeStatement(connection, query, fieldValues);
    }

    @Override
    public void update(Connection connection, T client) {
        String query = entitySQLMetaData.getUpdateSql();
        List<Object> params = new ArrayList<>();
        params.addAll(entityClassMetaData.getFieldValues(client, entityClassMetaData.getFieldsWithoutId()));
        params.addAll(entityClassMetaData.getFieldValues(client, List.of(entityClassMetaData.getIdField())));

        dbExecutor.executeStatement(connection, query, params);
    }

    private T parseEntity(ResultSet resultSet) throws ReflectiveOperationException, SQLException {
        T entity = entityClassMetaData.getConstructor().newInstance();
        List<Field> fields = entityClassMetaData.getAllFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = resultSet.getObject(fieldName);
            field.setAccessible(true);
            field.set(entity, fieldValue);
        }

        return entity;
    }
}
