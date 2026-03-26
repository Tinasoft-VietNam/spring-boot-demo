package com.tan.userservice.security;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class TypedListRedisSerializer<T> implements RedisSerializer<List<T>> {

    private final ObjectMapper objectMapper;
    private final JavaType targetType;

    public TypedListRedisSerializer(Class<T> elementType) {
        this.objectMapper = new ObjectMapper();
        this.targetType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
    }

    @Override
    public byte[] serialize(List<T> value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }

        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (Exception exception) {
            throw new SerializationException("Could not serialize list value", exception);
        }
    }

    @Override
    public List<T> deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(bytes, targetType);
        } catch (Exception exception) {
            throw new SerializationException("Could not deserialize list value", exception);
        }
    }
}
