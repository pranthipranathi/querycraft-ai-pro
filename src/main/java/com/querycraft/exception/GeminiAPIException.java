package com.querycraft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class GeminiAPIException extends RuntimeException {

    public GeminiAPIException(String message) {
        super(message);
    }
}