package com.tan.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tan.userservice.common.errors.ResourceNotFoundException;
import com.tan.userservice.entity.RolePermission;
import com.tan.userservice.entity.User;
import com.tan.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {
    private final UserRepository userRepository;
    private final TokenCacheService tokenCacheService;
    private final CacheManager cacheManager;

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.refresh-ttl-seconds:604800}")
    private long refreshTokenTtlSeconds;

    public TokenService(UserRepository userRepository, TokenCacheService tokenCacheService, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.tokenCacheService = tokenCacheService;
        this.cacheManager = cacheManager;
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            Map<String, Object> authzMatrix = getAuthzMatrix(user.getEmail());
            List<String> authorities = resolveAuthorities(authzMatrix);
            String role = resolveRole(authzMatrix);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("role", role)
                    .withClaim("authorities", authorities)
                    .withClaim("jti", UUID.randomUUID().toString())
                    .withClaim("token_type", "access")
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            String refreshToken = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("token_type", "refresh")
                    .withExpiresAt(generateRefreshExpirationDate())
                    .sign(algorithm);

            tokenCacheService.storeRefreshToken(hashToken(refreshToken), refreshTokenTtlSeconds);
            return refreshToken;

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating refresh token", exception);
        }
    }

    public String refreshToken(String refreshToken) {
        if (!tokenCacheService.refreshTokenExists(hashToken(refreshToken))) {
            log.warn("Refresh token not found in Redis state store");
            return null;
        }

        String email = validateRefreshToken(refreshToken);
        if (email == null) {
            return null;
        }
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email " + email + " not found"));
        return generateToken(user);
    }

    public boolean revokeRefreshToken(String refreshToken) {
        String email = validateRefreshToken(refreshToken);
        if (email == null) {
            return false;
        }

        String tokenHash = hashToken(refreshToken);
        if (!tokenCacheService.refreshTokenExists(tokenHash)) {
            return false;
        }

        tokenCacheService.removeRefreshToken(tokenHash);
        return true;
    }

    public boolean revokeAccessToken(String accessToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .withClaim("token_type", "access")
                    .build();

            DecodedJWT jwt = verifier.verify(accessToken);
            String jti = jwt.getClaim("jti").asString();
            Date expiresAt = jwt.getExpiresAt();

            if (jti == null || jti.isBlank() || expiresAt == null) {
                return false;
            }

            long remainingSeconds = Math.max(expiresAt.toInstant().getEpochSecond() - Instant.now().getEpochSecond(),
                    1);
            tokenCacheService.blacklistAccessToken(jti, remainingSeconds);
            return true;
        } catch (JWTVerificationException exception) {
            log.warn("Cannot revoke access token: invalid token", exception);
            return false;
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .withClaim("token_type", "access")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            String jti = jwt.getClaim("jti").asString();
            if (jti != null && tokenCacheService.isAccessTokenBlacklisted(jti)) {
                log.warn("Access token is blacklisted. jti={}", jti);
                return null;
            }

            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            log.error("Token verification failed: ", exception);
            return null;
        }
    }

    private String validateRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .withClaim("token_type", "refresh")
                    .build();

            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            log.error("Refresh token verification failed: ", exception);
            return null;
        }
    }

    private List<String> extractAuthorities(User user) {
        List<String> authorities = new ArrayList<>();
        authorities.add(normalizeRole(user.getRole().getName()));

        if (user.getRole().getRolePermissions() != null) {
            for (RolePermission rolePermission : user.getRole().getRolePermissions()) {
                if (rolePermission == null || rolePermission.getPermission() == null) {
                    continue;
                }

                String key = rolePermission.getPermission().getTable_key();
                if (key == null || key.isBlank()) {
                    continue;
                }

                String normalized = key.trim().toUpperCase();
                if (rolePermission.getIs_read() != null && rolePermission.getIs_read() == 1) {
                    authorities.add(normalized + "_READ");
                }
                if (rolePermission.getIs_create() != null && rolePermission.getIs_create() == 1) {
                    authorities.add(normalized + "_CREATE");
                }
                if (rolePermission.getIs_update() != null && rolePermission.getIs_update() == 1) {
                    authorities.add(normalized + "_UPDATE");
                }
                if (rolePermission.getIs_delete() != null && rolePermission.getIs_delete() == 1) {
                    authorities.add(normalized + "_DELETE");
                }
                if (rolePermission.getIs_manage() != null && rolePermission.getIs_manage() == 1) {
                    authorities.add(normalized + "_MANAGE");
                }
            }
        }

        return authorities.stream().distinct().toList();
    }

    private String normalizeRole(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return "ROLE_USER";
        }

        String normalized = roleName.trim().replace('-', '_').replace(' ', '_').toUpperCase();
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }

    private Instant generateExpirationDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).plusHours(2).toInstant();
    }

    private Instant generateRefreshExpirationDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).plusDays(7).toInstant();
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm is not available", ex);
        }
    }

    private Map<String, Object> getAuthzMatrix(String email) {
        Cache cache = cacheManager.getCache("authz:matrix");
        if (cache != null) {
            Cache.ValueWrapper cacheValue = cache.get(email);
            if (cacheValue != null && cacheValue.get() instanceof Map<?, ?> rawMap) {
                Map<String, Object> cached = new HashMap<>();
                rawMap.forEach((key, value) -> cached.put(String.valueOf(key), value));
                cached.put("role", resolveRole(cached));
                cached.put("authorities", resolveAuthorities(cached));
                return cached;
            }
        }

        User fullUser = userRepository.findWithRolePermissionsByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        Map<String, Object> authzMatrix = Map.of(
                "role", normalizeRole(fullUser.getRole().getName()),
                "authorities", extractAuthorities(fullUser));

        if (cache != null) {
            cache.put(email, authzMatrix);
        }
        return authzMatrix;
    }

    private String resolveRole(Map<String, Object> authzMatrix) {
        Object roleRaw = authzMatrix.get("role");
        return normalizeRole(roleRaw == null ? null : String.valueOf(roleRaw));
    }

    private List<String> resolveAuthorities(Map<String, Object> authzMatrix) {
        Object authoritiesRaw = authzMatrix.get("authorities");
        if (authoritiesRaw instanceof List<?> rawList) {
            return rawList.stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .distinct()
                    .toList();
        }

        return List.of();
    }
}
