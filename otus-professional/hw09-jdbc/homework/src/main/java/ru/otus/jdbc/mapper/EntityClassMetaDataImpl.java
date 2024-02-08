package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;
import ru.otus.jdbc.mapper.exception.EntityClassMetaDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final String CONSTRUCTOR_WITHOUT_PARAMETERS_EXCEPTION = "The class %s should have a constructor without parameters.";
    private static final String ID_EXCEPTION = "The class %s should have a single field with an id annotation.";
    private static final String FIELD_ACCESS_EXCEPTION = "Can not get values of the class %s instance fields";
    private final Class<T> clazz;

    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutIdField;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        List<Field> allFields = List.of(clazz.getDeclaredFields());
        Field idField = allFields.stream()
                .filter((field -> field.isAnnotationPresent(Id.class)))
                .findFirst()
                .orElseThrow(() -> new EntityClassMetaDataException(String.format(ID_EXCEPTION, clazz.getName())));
        List<Field> fieldsWithoutIdField = allFields.stream()
                .filter((field -> !field.isAnnotationPresent(Id.class)))
                .toList();

        this.clazz = clazz;
        this.idField = idField;
        this.allFields = allFields;
        this.fieldsWithoutIdField = fieldsWithoutIdField;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new EntityClassMetaDataException(String.format(CONSTRUCTOR_WITHOUT_PARAMETERS_EXCEPTION, clazz.getName()), e);
        }
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
        return fieldsWithoutIdField;
    }

    @Override
    public List<Object> getFieldValues(T entity, List<Field> fields) {
        return fields.stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new EntityClassMetaDataException(String.format(FIELD_ACCESS_EXCEPTION, clazz.getName()), e);
                    }
                })
                .collect(Collectors.toList());
    }
}
