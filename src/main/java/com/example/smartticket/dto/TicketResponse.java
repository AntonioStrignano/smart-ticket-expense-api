package com.example.smartticket.dto;

import java.time.LocalDateTime;

import com.example.smartticket.enums.TicketCategory;
import com.example.smartticket.enums.TicketPriority;
import com.example.smartticket.enums.TicketStatus;

public record TicketResponse(

        Long id,
        String title,
        String description,
        TicketCategory category,
        TicketPriority priority,
        TicketStatus status,
        LocalDateTime createdAt
) {
}