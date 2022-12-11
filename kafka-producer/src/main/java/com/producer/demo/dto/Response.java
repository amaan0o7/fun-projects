package com.producer.demo.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    private String message;
    private Object result;
    private HttpStatus status;
}
