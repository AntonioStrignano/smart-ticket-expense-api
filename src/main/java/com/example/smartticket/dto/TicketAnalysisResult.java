package com.example.smartticket.dto;

import com.example.smartticket.enums.TicketCategory;
import com.example.smartticket.enums.TicketPriority;

public record TicketAnalysisResult(
        TicketCategory category,
        TicketPriority priority,
        String summary
) {
}