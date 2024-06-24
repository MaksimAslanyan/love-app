package com.example.datinguserapispring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {


    private HttpStatus status;
    private Integer errorCode;
    private Instant timestamp;
    private String message;
}
