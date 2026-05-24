package com.example.JournalApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception ex){

        Map<String,Object> map =
                new HashMap<>();

        map.put("message",ex.getMessage());

        map.put("timestamp",
                LocalDateTime.now());

        return new ResponseEntity<>(
                map,
                HttpStatus.BAD_REQUEST
        );
    }
}