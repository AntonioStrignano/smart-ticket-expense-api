package com.example.smartticket.dto;

import com.example.smartticket.enums.TicketCategory;
import com.example.smartticket.enums.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketRequest(

        @NotBlank(message = "Il titolo è obbligatorio")
        @Size(max = 100, message = "Il titolo non può superare 100 caratteri")
        String title,

        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(max = 500, message = "La descrizione non può superare 500 caratteri")
        String description,

        @NotNull(message = "La categoria è obbligatoria")
        TicketCategory category,

        @NotNull(message = "La priorità è obbligatoria")
        TicketPriority priority
) {
}