package com.example.businessservice.infrastructure.config.common.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorMessage;
    private String field;
}

