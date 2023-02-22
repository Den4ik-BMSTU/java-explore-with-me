package ru.practicum.exceptions;

import jdk.jfr.StackTrace;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final String error;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public ErrorResponse(String error, HttpStatus status, LocalDateTime timestamp) {
        this.error = error;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getStatus() {
            return status;
    }

    public LocalDateTime getTimestamp() {
            return timestamp;
    }
}