package com.tan.userservice.security;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import tools.jackson.databind.ObjectMapper;

public class TypedRedisSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> targetType;

    public TypedRedisSerializer(Class<T> targetType) {
        this.objectMapper = new ObjectMapper();
        this.targetType = targetType;
    }

    @Override
    public byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }

        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (Exception exception) {
            throw new SerializationException("Could not serialize value for " + targetType.getSimpleName(), exception);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(bytes, targetType);
        } catch (Exception exception) {
            throw new SerializationException("Could not deserialize value for " + targetType.getSimpleName(),
                    exception);
        }
    }
}
