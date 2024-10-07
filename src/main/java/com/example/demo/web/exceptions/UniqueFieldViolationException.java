package com.example.demo.web.exceptions;

public class UniqueFieldViolationException extends RuntimeException {
    public UniqueFieldViolationException(String message) {
        super(message);
    }
}
