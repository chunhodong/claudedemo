package com.example.claudeapi.fee.domain;

public class FeeNotFoundException extends RuntimeException {
    public FeeNotFoundException(Long id) {
        super("Fee not found with id: " + id);
    }
}
