package com.example.smartticket.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}