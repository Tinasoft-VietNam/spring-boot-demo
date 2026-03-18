package com.chemical.common.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LogicException extends RuntimeException {

    public LogicException(String exception) {
        super(exception);
    }
}

