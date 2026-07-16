package com.example.smartticket.dto;

public record LoginRequest(
        String username,
        String password
) {
}