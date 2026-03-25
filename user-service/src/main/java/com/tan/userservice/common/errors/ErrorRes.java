package com.tan.userservice.common.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorRes {
    private String data;
    private int status_code;
    private String message;
}
