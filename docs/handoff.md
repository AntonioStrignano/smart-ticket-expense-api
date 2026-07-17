# Handoff — Smart Ticket & Expense API

**Last Updated:** 2026-07-17

---

## Stato Corrente

| Aspetto | Status |
|---|---|
| Fase corrente | Fase 6 — Integrazione AI |
| Completamento | ✅ 100% Fase 5 |
| Blocker | Nessuno per Fase 6; Testcontainers 1.19.8 resta incompatibile con Docker API 1.55 locale |

---

## Completato

- [x] Documentazione (`docs/pipeline.md`, `docs/stack-and-structure.md`)
- [x] Docker PostgreSQL avviato (`smart-ticket-db`)
- [x] Struttura progetto Maven creata
- [x] `pom.xml` con dipendenze (Spring Boot 3.3.0, Security, JPA, AI, Swagger)
- [x] `application.properties` configurato
- [x] Classe main `SmartTicketExpenseApiApplication`
- [x] Istruzioni Copilot impostate
- [x] Documento di pianificazione operativo creato (`docs/project-plan.md`)
- [x] Diagnosi ambiente locale eseguita (`java`/`mvn` assenti)
- [x] Installazione `openjdk@21` + `maven` completata via Homebrew
- [x] Task 3 Fase 1 completato: DB `smart_ticket_db` verificato e raggiungibile
- [x] Ambiente shell aggiornato con PATH/JAVA_HOME persistenti
- [x] Correzione XML `pom.xml` (`&amp;` nel campo `name`)
- [x] Dipendenza Spring AI configurata (`spring-ai.version=1.0.0`, starter Ollama)
- [x] Build Maven verificata con successo (`mvn clean verify -DskipTests`)
- [x] Entity JPA `User` e `Ticket` implementate con enum di dominio
- [x] Relazione bidirezionale `User` 1-N `Ticket` implementata (`tickets.user_id`)
- [x] Repository Spring Data JPA `UserRepository` e `TicketRepository` implementati
- [x] Applicazione avviata e schema Hibernate verificato su PostgreSQL (`users`, `tickets` e foreign key)
- [x] Fase 3 completata: `JwtService` con firma HMAC-SHA-256 e validazione token
- [x] Fase 3 completata: `CustomUserDetailsService` basato su `UserRepository`
- [x] Fase 3 completata: `JwtAuthenticationFilter` per header Bearer e `SecurityContextHolder`
- [x] Fase 3 completata: `SecurityFilterChain` stateless con rotte auth e Swagger pubbliche
- [x] Verifica Fase 3: `mvn test` concluso con `BUILD SUCCESS`
- [x] Fase 4 completata: DTO `RegisterRequest`, `LoginRequest` e `AuthResponse`
- [x] Fase 4 completata: `AuthService` con registrazione BCrypt, ruolo iniziale `USER` e login JWT
- [x] Fase 4 completata: `AuthController` con `POST /api/auth/register` e `POST /api/auth/login`
- [x] Gestione errori auth corretta: `/error` consentito dalla security per mantenere `401 Unauthorized`
- [x] Verifica Fase 4 su PostgreSQL locale: registrazione `201`, login valido `200`, login con password errata `401`
- [x] Fase 5: DTO `TicketRequest` e `TicketResponse` definiti
- [x] Fase 5: `TicketService`, `TicketController` e autorizzazione `ADMIN` sul delete implementati
- [x] Fase 5: test di integrazione `TicketControllerIntegrationTest` creato e compilato
- [x] Fase 5 verificata manualmente su PostgreSQL locale: create `201`, lista proprietario `200`, ticket altrui `404`, delete USER `403`, delete ADMIN `204`, payload non valido `400`
- [x] Documentazione AI allineata a Ollama locale (`qwen2.5:3b`); OpenAI non e utilizzato

---

## In Corso

- Nessuna attivita in corso

---

## Prossimo

- Configurare `ChatClient` bean di Spring AI
- Definire `TicketAnalysisResult` e implementare `AiService`
- Aggiungere `POST /api/tickets/analyze`

---

## Note

Progetto in: `/Users/antonio/Desktop/dev/smart-ticket-expense-api`

Testcontainers 1.19.8, gestito da Spring Boot 3.3.0, riceve `400 Bad Request` da Docker Desktop 29.6.1 con API 1.55 prima dell'avvio del container. `mvn test -DskipTests` compila correttamente sorgenti principali e test.
