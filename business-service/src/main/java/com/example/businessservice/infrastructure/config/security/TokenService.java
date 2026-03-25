package com.example.businessservice.infrastructure.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenService {

    // Lưu ý nhỏ: Ở môi trường thực tế (Production), bạn nên dùng ${jwt.secret}
    // và cấu hình trong file application.yml thay vì fix cứng chuỗi này nhé.
    @Value("Y7dQGhZdKNERmREIlnQxoJSlLaeozDb6tOwrtONAVTvCqZRjw0oXX2aFUxj6YlGK")
    private String secret;

    /**
     * Kiểm tra xem Token có hợp lệ không (đúng chữ ký, chưa hết hạn, đúng issuer)
     */
    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            log.error("Token verification failed: ", exception);
            return null;
        }
    }

    /**
     * Lấy Subject (thường là email hoặc user ID) từ Token
     */
    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    /**
     * MỚI: Lấy danh sách quyền (Roles) từ Token để làm phân quyền (Authorization)
     * Giả định rằng Auth Service lúc tạo Token đã nhét danh sách role vào claim tên là "roles"
     */
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);

            // Đọc claim "roles" dưới dạng List<String>
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            if (roles == null || roles.isEmpty()) {
                return Collections.emptyList();
            }

            // Chuyển đổi từ String sang SimpleGrantedAuthority của Spring Security
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        } catch (JWTVerificationException exception) {
            return Collections.emptyList();
        }
    }

}