package com.tan.userservice.common.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public enum FilterOperator {
    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Expression<?> key = root.get(request.getProperty());
            Object value = convertValue(key, request.getValue());
            return cb.and(cb.equal(key, value), predicate);
        }
    },

    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Expression<?> key = root.get(request.getProperty());
            Object value = convertValue(key, request.getValue());
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Expression<String> key = root.get(request.getProperty());
            Object value = unwrapRequestValue(request.getValue());
            return cb.and(cb.like(cb.upper(key), "%" + String.valueOf(value).toUpperCase() + "%"), predicate);
        }
    };

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);

    private static Object convertValue(Expression<?> key, JsonNode rawValue) {
        Object value = unwrapRequestValue(rawValue);
        if (value == null) {
            return null;
        }

        Class<?> targetType = key.getJavaType();
        if (targetType == null || targetType.isInstance(value)) {
            return value;
        }

        String textValue = String.valueOf(value);

        try {
            if (String.class.equals(targetType)) {
                return textValue;
            }
            if (Integer.class.equals(targetType) || int.class.equals(targetType)) {
                return Integer.valueOf(textValue);
            }
            if (Long.class.equals(targetType) || long.class.equals(targetType)) {
                return Long.valueOf(textValue);
            }
            if (Double.class.equals(targetType) || double.class.equals(targetType)) {
                return Double.valueOf(textValue);
            }
            if (Float.class.equals(targetType) || float.class.equals(targetType)) {
                return Float.valueOf(textValue);
            }
            if (Boolean.class.equals(targetType) || boolean.class.equals(targetType)) {
                return Boolean.valueOf(textValue);
            }
            if (Short.class.equals(targetType) || short.class.equals(targetType)) {
                return Short.valueOf(textValue);
            }
            if (Byte.class.equals(targetType) || byte.class.equals(targetType)) {
                return Byte.valueOf(textValue);
            }
            if (Character.class.equals(targetType) || char.class.equals(targetType)) {
                return textValue.isEmpty() ? null : textValue.charAt(0);
            }
            if (UUID.class.equals(targetType)) {
                return UUID.fromString(textValue);
            }
            if (LocalDate.class.equals(targetType)) {
                return LocalDate.parse(textValue);
            }
            if (LocalDateTime.class.equals(targetType)) {
                return LocalDateTime.parse(textValue);
            }
            if (Instant.class.equals(targetType)) {
                return Instant.parse(textValue);
            }
            if (targetType.isEnum()) {
                String normalizedEnumValue = textValue.trim();
                Object[] constants = targetType.getEnumConstants();
                if (constants != null) {
                    for (Object constant : constants) {
                        Enum<?> enumConstant = (Enum<?>) constant;
                        if (enumConstant.name().equalsIgnoreCase(normalizedEnumValue)) {
                            return enumConstant;
                        }
                    }
                }
                throw new IllegalArgumentException("Invalid enum value: " + normalizedEnumValue);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Cannot convert value '" + textValue + "' to type " + targetType.getSimpleName(), ex);
        }

        return value;
    }

    private static Object unwrapRequestValue(JsonNode rawValue) {
        if (rawValue == null || rawValue.isNull()) {
            return null;
        }
        if (rawValue.isTextual()) {
            return rawValue.asText();
        }
        if (rawValue.isBoolean()) {
            return rawValue.asBoolean();
        }
        if (rawValue.isInt()) {
            return rawValue.asInt();
        }
        if (rawValue.isLong()) {
            return rawValue.asLong();
        }
        if (rawValue.isFloat() || rawValue.isDouble() || rawValue.isBigDecimal()) {
            return rawValue.decimalValue();
        }
        if (rawValue.isBigInteger()) {
            return rawValue.bigIntegerValue();
        }

        return rawValue.toString();
    }
}
