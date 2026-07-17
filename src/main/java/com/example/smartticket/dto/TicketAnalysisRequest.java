package com.example.smartticket.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketAnalysisRequest(
        @NotBlank(message = "Il testo da analizzare e obbligatorio")
        String rawText
) {
}