package com.example.smartticket.controller;

import com.example.smartticket.dto.TicketRequest;
import com.example.smartticket.entity.Ticket;
import com.example.smartticket.entity.User;
import com.example.smartticket.enums.Role;
import com.example.smartticket.enums.TicketCategory;
import com.example.smartticket.enums.TicketPriority;
import com.example.smartticket.enums.TicketStatus;
import com.example.smartticket.repository.TicketRepository;
import com.example.smartticket.repository.UserRepository;
import com.example.smartticket.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TicketControllerIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresql::getJdbcUrl);
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("spring.datasource.password", postgresql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @AfterEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void authenticatedUserCreatesAndListsOnlyOwnTickets() throws Exception {
        User alice = createUser("alice", Role.USER);
        User bob = createUser("bob", Role.USER);
        createTicket(bob, "Ticket di Bob");

        TicketRequest request = new TicketRequest(
                "Errore di accesso",
                "Non riesco ad accedere al portale",
                TicketCategory.BUG,
                TicketPriority.HIGH
        );

        mockMvc.perform(post("/api/tickets")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(alice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Errore di accesso"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        mockMvc.perform(get("/api/tickets")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(alice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Errore di accesso"));
    }

    @Test
    void userCannotReadAnotherUsersTicket() throws Exception {
        User alice = createUser("alice", Role.USER);
        User bob = createUser("bob", Role.USER);
        Ticket bobsTicket = createTicket(bob, "Ticket privato");

        mockMvc.perform(get("/api/tickets/{ticketId}", bobsTicket.getId())
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(alice)))
                .andExpect(status().isNotFound());
    }

    @Test
    void onlyAdminCanDeleteTicket() throws Exception {
        User user = createUser("user", Role.USER);
        User admin = createUser("admin", Role.ADMIN);
        Ticket ticket = createTicket(user, "Ticket da eliminare");

        mockMvc.perform(delete("/api/tickets/{ticketId}", ticket.getId())
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(user)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/tickets/{ticketId}", ticket.getId())
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(admin)))
                .andExpect(status().isNoContent());

        org.junit.jupiter.api.Assertions.assertFalse(ticketRepository.existsById(ticket.getId()));
    }

    private User createUser(String username, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(role);
        return userRepository.save(user);
    }

    private Ticket createTicket(User user, String title) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription("Descrizione di test");
        ticket.setCategory(TicketCategory.SUPPORT);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setUser(user);
        return ticketRepository.save(ticket);
    }

    private String bearerToken(User user) {
        return "Bearer " + jwtService.generateToken(user.getUsername(), user.getRole().name());
    }
}