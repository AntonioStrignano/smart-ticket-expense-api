package com.example.smartticket.service;

import java.util.List;

import com.example.smartticket.dto.TicketRequest;
import com.example.smartticket.dto.TicketResponse;
import com.example.smartticket.entity.Ticket;
import com.example.smartticket.entity.User;
import com.example.smartticket.enums.TicketStatus;
import com.example.smartticket.repository.TicketRepository;
import com.example.smartticket.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketResponse createTicket(TicketRequest request, String username) {
        User user = findUserByUsername(username);

        Ticket ticket = new Ticket();

        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setCategory(request.category());
        ticket.setPriority(request.priority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setUser(user);

        return toTicketResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getTickets(String username) {
        User user = findUserByUsername(username);

        return ticketRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toTicketResponse)
                .toList();
    }

    public TicketResponse getTicketById(Long ticketId, String username) {
        User user = findUserByUsername(username);

        Ticket ticket = ticketRepository.findByIdAndUserId(ticketId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ticket non trovato"
                ));

        return toTicketResponse(ticket);
    }

    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ticket non trovato"
                ));

        ticketRepository.delete(ticket);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Utente non autenticato"
                ));
    }

    private TicketResponse toTicketResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedAt()
        );
    }
}