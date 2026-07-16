package com.example.smartticket.controller;

import java.util.List;

import com.example.smartticket.dto.TicketRequest;
import com.example.smartticket.dto.TicketResponse;
import com.example.smartticket.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(
            @Valid @RequestBody TicketRequest request,
            Authentication authentication
    ) {
        return ticketService.createTicket(request, authentication.getName());
    }

    @GetMapping
    public List<TicketResponse> getTickets(Authentication authentication) {
        return ticketService.getTickets(authentication.getName());
    }

    @GetMapping("/{ticketId}")
    public TicketResponse getTicketById(
            @PathVariable Long ticketId,
            Authentication authentication
    ) {
        return ticketService.getTicketById(ticketId, authentication.getName());
    }

    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }
}