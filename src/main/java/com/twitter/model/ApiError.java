package com.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiError {

    private HttpStatus status;
    private String timestamp;
    private String message;

    public ApiError(HttpStatus status, Throwable ex) {
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
        this.message = ex.getMessage();
    }
}