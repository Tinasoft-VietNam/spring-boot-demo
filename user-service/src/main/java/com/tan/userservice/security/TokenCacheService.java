package com.tan.userservice.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {
    private static final String REFRESH_TOKEN_PREFIX = "token:refresh:";
    private static final String ACCESS_BLACKLIST_PREFIX = "token:blacklist:access:";

    private final RedisTemplate<String, String> redisTemplate;

    public TokenCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeRefreshToken(String tokenHash, long expirationSeconds) {
        String key = REFRESH_TOKEN_PREFIX + tokenHash;
        redisTemplate.opsForValue().set(key, "active", expirationSeconds, TimeUnit.SECONDS);
    }

    public boolean refreshTokenExists(String tokenHash) {
        String key = REFRESH_TOKEN_PREFIX + tokenHash;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void removeRefreshToken(String tokenHash) {
        String key = REFRESH_TOKEN_PREFIX + tokenHash;
        redisTemplate.delete(key);
    }

    public void blacklistAccessToken(String jti, long expirationSeconds) {
        String key = ACCESS_BLACKLIST_PREFIX + jti;
        long ttl = Math.max(expirationSeconds, 1);
        redisTemplate.opsForValue().set(key, "revoked", ttl, TimeUnit.SECONDS);
    }

    public boolean isAccessTokenBlacklisted(String jti) {
        String key = ACCESS_BLACKLIST_PREFIX + jti;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}