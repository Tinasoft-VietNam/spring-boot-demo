package com.tan.userservice.security;

import com.tan.userservice.common.errors.ErrorRes;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        String errorMessage;
        if (authException != null && authException.getCause() != null) {
            errorMessage = authException.getCause().getMessage();
        } else {
            errorMessage = "Authentication failed: " + authException.getMessage();
        }
        ErrorRes errorResponse = new ErrorRes(errorMessage, HttpStatus.UNAUTHORIZED.value(), "error");
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonErrorResponse);
    }
}