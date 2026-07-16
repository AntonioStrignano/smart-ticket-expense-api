# Handoff — Smart Ticket & Expense API

**Last Updated:** 2026-07-16

---

## Stato Corrente

| Aspetto | Status |
|---|---|
| Fase corrente | Fase 3 — Sicurezza (JWT + Spring Security) |
| Completamento | ✅ 100% Fase 2 |
| Blocker | Nessuno |

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
- [x] Dipendenza Spring AI corretta (`spring-ai.version=1.0.0`, starter OpenAI aggiornato)
- [x] Build Maven verificata con successo (`mvn clean verify -DskipTests`)
- [x] Entity JPA `User` e `Ticket` implementate con enum di dominio
- [x] Relazione bidirezionale `User` 1-N `Ticket` implementata (`tickets.user_id`)
- [x] Repository Spring Data JPA `UserRepository` e `TicketRepository` implementati
- [x] Applicazione avviata e schema Hibernate verificato su PostgreSQL (`users`, `tickets` e foreign key)

---

## In Corso

- Avviare Fase 3: implementazione di `JwtService`

---

## Prossimo

- Implementare `JwtAuthenticationFilter`
- Configurare `SecurityFilterChain` stateless e `UserDetailsService` custom

---

## Note

Progetto in: `/Users/antonio/Desktop/dev/smart-ticket-expense-api`
