package ru.rodionov.starter.util;

import jakarta.persistence.AttributeConverter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericEnumConverter<E extends Enum<E>, T> implements AttributeConverter<E, T> {

    private static final String VALUE_FIELD_NAME = "value";

    private final Map<T, E> dbToEnum = new HashMap<>();
    private final Map<E, T> enumToDb = new HashMap<>();

    public GenericEnumConverter(Class<E> enumClass) {

        Field valueField;
        try {
            valueField = enumClass.getDeclaredField(VALUE_FIELD_NAME);
            valueField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Enum must have a field named '" + VALUE_FIELD_NAME + "'", e);
        }

        for (E constant : enumClass.getEnumConstants()) {
            try {
                @SuppressWarnings("unchecked")
                T value = (T) valueField.get(constant);
                enumToDb.put(constant, value);
                dbToEnum.put(value, constant);
            } catch (IllegalAccessException | ClassCastException ex) {
                throw new IllegalStateException("Can't read or cast field 'value' from enum", ex);
            }
        }
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : enumToDb.get(attribute);
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        return dbData == null ? null : dbToEnum.get(dbData);
    }
}