# Pipeline di Build — Smart Ticket & Expense API

## Fase 1 — Scaffolding & Configurazione
- [x] 1. Generare il progetto via Spring Initializr con le dipendenze corrette
- [x] 2. Configurare `application.properties` (DB, JWT secret, OpenAI key)
- [x] 3. Verificare connessione PostgreSQL e creare il database `smart_ticket_db`

## Fase 2 — Modello Dati & Persistenza
- [x] 4. Definire entity `User` + `Ticket` con annotazioni JPA
- [x] 5. Definire le relazioni (`@ManyToOne`, `@OneToMany`)
- [x] 6. Creare i `Repository` (Spring Data JPA)
- [x] 7. Verificare che Hibernate generi le tabelle correttamente (`ddl-auto=update`)

## Fase 3 — Sicurezza (JWT + Spring Security)
- [x] 8. Implementare `JwtService` (generazione + validazione token, firma HMAC-256)
- [x] 9. Implementare `JwtAuthenticationFilter` (estrazione Bearer dall'header)
- [x] 10. Configurare `SecurityFilterChain` (stateless, rotte pubbliche vs protette)
- [x] 11. Implementare `UserDetailsService` custom

## Fase 4 — Auth Endpoints
- [ ] 12. Definire i Record DTO: `RegisterRequest`, `LoginRequest`, `AuthResponse`
- [ ] 13. Implementare `AuthService` (register con BCrypt, login con token)
- [ ] 14. Implementare `AuthController` (`/api/auth/register`, `/api/auth/login`)
- [ ] 15. Testare con Postman/curl

## Fase 5 — Ticket CRUD
- [ ] 16. Definire i Record DTO: `TicketRequest`, `TicketResponse`
- [ ] 17. Implementare `TicketService` (CRUD filtrato per utente loggato)
- [ ] 18. Implementare `TicketController` con i 4 endpoint + `@PreAuthorize` per `ADMIN`

## Fase 6 — Integrazione AI
- [ ] 19. Configurare `ChatClient` bean di Spring AI
- [ ] 20. Definire il Record `TicketAnalysisResult(category, priority, summary)`
- [ ] 21. Implementare `AiService` con `BeanOutputConverter` per Structured Output
- [ ] 22. Aggiungere endpoint `POST /api/tickets/analyze` al controller

## Fase 7 — Documentazione & Rifinitura
- [ ] 23. Configurare Springdoc/Swagger UI con security scheme JWT
- [ ] 24. Aggiungere `@Operation` e `@SecurityRequirement` ai controller
- [ ] 25. Test end-to-end dell'intero flusso
