package com.tan.userservice.security;

import com.tan.userservice.dto.response.PermissionResponseDTO;
import com.tan.userservice.dto.response.RoleResponseDTO;
import com.tan.userservice.dto.response.UserResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer serializer = new StringRedisSerializer();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJacksonJsonRedisSerializer defaultValueSerializer = new GenericJacksonJsonRedisSerializer(
                new ObjectMapper());
        GenericJacksonJsonRedisSerializer mapValueSerializer = new GenericJacksonJsonRedisSerializer(
                new ObjectMapper());
        RedisSerializer<UserResponseDTO> userResponseSerializer = new TypedRedisSerializer<>(UserResponseDTO.class);
        RedisSerializer<RoleResponseDTO> roleResponseSerializer = new TypedRedisSerializer<>(RoleResponseDTO.class);
        RedisSerializer<java.util.List<RoleResponseDTO>> roleResponseListSerializer = new TypedListRedisSerializer<>(
                RoleResponseDTO.class);
        RedisSerializer<java.util.List<PermissionResponseDTO>> permissionResponseListSerializer = new TypedListRedisSerializer<>(
                PermissionResponseDTO.class);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(defaultValueSerializer))
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> "usrsvc:" + cacheName + "::")
                .entryTtl(Duration.ofMinutes(5));

        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();
        cacheConfig.put("role:all", defaultCacheConfig
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(roleResponseListSerializer)));
        cacheConfig.put("role:detail", defaultCacheConfig
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(roleResponseSerializer)));
        cacheConfig.put("permission:all", defaultCacheConfig
                .entryTtl(Duration.ofMinutes(15))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(permissionResponseListSerializer)));
        cacheConfig.put("permission:detail", defaultCacheConfig
                .entryTtl(Duration.ofMinutes(15))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(permissionResponseListSerializer)));
        cacheConfig.put("user:me", defaultCacheConfig
                .entryTtl(Duration.ofMinutes(3))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(userResponseSerializer)));
        cacheConfig.put("authz:matrix", defaultCacheConfig
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(mapValueSerializer)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfig)
                .build();
    }
}