package com.tan.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tan.userservice.common.errors.ResourceNotFoundException;
import com.tan.userservice.entity.RolePermission;
import com.tan.userservice.entity.User;
import com.tan.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TokenService {
    private final UserRepository userRepository;
    @Value("${api.security.token.secret}")
    private String secret;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            User fullUser = userRepository.findWithRolePermissionsByEmail(user.getEmail())
                .orElse(user);

            List<String> authorities = extractAuthorities(fullUser);
            String role = normalizeRole(fullUser.getRole().getName());

            return JWT.create()
                    .withIssuer("auth-api")
                .withSubject(fullUser.getEmail())
                    .withClaim("role", role)
                    .withClaim("authorities", authorities)
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
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("token_type", "refresh")
                    .withExpiresAt(generateRefreshExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating refresh token", exception);
        }
    }

    public String refreshToken(String refreshToken) {
        String email = validateRefreshToken(refreshToken);
        if (email == null) {
            return null;
        }
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email " + email + " not found")
        );
        return generateToken(user);
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .withClaim("token_type", "access")
                    .build();

            return verifier.verify(token).getSubject();
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
}
