package com.tan.userservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        List<String> authorityClaims = jwt.getClaimAsStringList("authorities");
        if (authorityClaims != null) {
            authorityClaims.stream()
                    .filter(value -> value != null && !value.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        String role = jwt.getClaimAsString("role");
        if (role != null && !role.isBlank()) {
            String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
            authorities.add(new SimpleGrantedAuthority(normalizedRole));
        }

        return authorities;
    }
}
