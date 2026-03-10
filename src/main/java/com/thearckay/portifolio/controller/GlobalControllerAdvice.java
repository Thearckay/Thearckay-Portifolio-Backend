package com.thearckay.portifolio.controller;

import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.exceptions.LoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiResponse> loginErrorHandler(LoginException error){
        ApiResponse response = new ApiResponse(
                error.getStatus(),
                Collections.emptyList(),
                error.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(error.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> generalErrorHandler(RuntimeException error){
        ApiResponse response = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Collections.emptyList(),
                error.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
    }
}
