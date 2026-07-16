# Project Plan Operativo — Smart Ticket & Expense API

**Data:** 2026-07-16  
**Obiettivo:** costruire il progetto in modo sequenziale, con dipendenze esplicite, per evitare rework.

---

## 1) Visione e confini

### Scope in questa iterazione
- API REST stateless con autenticazione JWT.
- Gestione ticket utente (CRUD base, con regole ruolo).
- Endpoint AI per analisi testo non strutturato con output JSON strutturato.
- Persistenza PostgreSQL via JPA/Hibernate.
- Documentazione API via Swagger/OpenAPI.

### Out of scope iniziale
- Frontend.
- Workflow BPM complessi.
- Queue/event-driven architecture.
- Multi-tenant e audit avanzato.

---

## 2) Stato attuale verificato

- Presente scaffolding Maven/Spring Boot e `application.properties`.
- Dipendenze principali già in `pom.xml` (Web, Security, JPA, PostgreSQL, Spring AI, Swagger, JJWT).
- Non risultano ancora classi applicative in `src/main/java` (controller, service, entity, security).

Impatto: serve un avvio ordinato partendo da modello dati e sicurezza, prima di endpoint business.

---

## 3) Principi architetturali da rispettare

- Stateless completo (`SessionCreationPolicy.STATELESS`).
- DTO solo con Java Records.
- Separazione netta Controller -> Service -> Repository.
- Output AI tipizzato (no testo libero) con schema JSON rigido mappato su record.
- Validazioni input in ingresso (Bean Validation).
- Error handling coerente (response model uniforme per errori).

---

## 4) Piano per fasi (con dipendenze)

## Fase 0 — Baseline tecnica

### Obiettivo
Portare il progetto in uno stato buildabile e ripetibile localmente.

### Attività
- Verificare build Maven (`mvn clean verify`).
- Verificare connessione PostgreSQL locale su `smart_ticket_db`.
- Confermare variabili e secret minimi (`OPENAI_API_KEY`, `jwt.secret`).
- Definire convenzioni package, naming e formato response errori.

### Dipende da
- Nessuna.

### Deliverable
- Build verde.
- Ambiente locale documentato e riproducibile.

### Definition of Done
- Progetto compila senza warning bloccanti.
- Applicazione avvia su porta 8080.

---

## Fase 1 — Dominio e persistenza

### Obiettivo
Stabilire il modello dati stabile prima della logica business.

### Stato
Completata il 2026-07-16: entity, enum, repository e schema PostgreSQL verificati tramite avvio dell'applicazione con `ddl-auto=update`.

### Attività
- Implementare `User` e `Ticket` con vincoli JPA.
- Definire relazioni (`User` 1-N `Ticket`).
- Aggiungere enum di dominio: `Role`, `TicketStatus`, `TicketPriority`.
- Creare `UserRepository` e `TicketRepository`.
- Validare schema generato su DB.

### Dipende da
- Fase 0.

### Deliverable
- Modello persistente pronto.

### Definition of Done
- Tabelle coerenti con schema atteso.
- Query base repository funzionanti con test di persistenza.

---

## Fase 2 — Sicurezza e autenticazione

### Obiettivo
Abilitare accesso sicuro all'API prima dei controller business.

### Attività
- Implementare `JwtService` (issue/validate token, claims minime).
- Implementare `JwtAuthenticationFilter`.
- Implementare `UserDetailsService` custom.
- Configurare `SecurityConfig` con rotte pubbliche/protette.
- Implementare `AuthService` + `AuthController` (`/register`, `/login`).

### Dipende da
- Fase 1 (serve `UserRepository`).

### Deliverable
- Flusso register/login funzionante con token JWT.

### Definition of Done
- Endpoint auth testati con casi positivi/negativi.
- Endpoint protetti rifiutano richieste senza token valido.

---

## Fase 3 — Ticket API core

### Obiettivo
Rilasciare il ciclo ticket con policy autorizzative chiare.

### Attività
- Definire DTO record: request/response ticket.
- Implementare `TicketService` con ownership check.
- Implementare `TicketController`:
  - `GET /api/tickets`
  - `POST /api/tickets`
  - `GET /api/tickets/{id}`
  - `DELETE /api/tickets/{id}` (solo ADMIN)
- Aggiungere validazioni request e gestione errori (`404`, `403`, `400`).

### Dipende da
- Fase 2.

### Deliverable
- API ticket stabile e coerente con i ruoli.

### Definition of Done
- Test di autorizzazione e ownership passano.
- Contratti JSON endpoint allineati alla documentazione.

---

## Fase 4 — Integrazione AI strutturata

### Obiettivo
Introdurre AI solo dopo base applicativa stabile.

### Attività
- Definire record `TicketAnalysisResult`.
- Implementare `AiService` con `ChatClient` + `BeanOutputConverter`.
- Implementare `POST /api/tickets/analyze`.
- Gestire failure mode AI (timeout, output non valido, fallback errore chiaro).

### Dipende da
- Fase 3.

### Deliverable
- Endpoint AI con output sempre tipizzato.

### Definition of Done
- L'endpoint non restituisce testo libero del modello.
- Risposta mappata sempre nel record previsto o errore standard.

---

## Fase 5 — Qualita, documentazione, hardening

### Obiettivo
Chiudere la prima release con affidabilita minima da sviluppo reale.

### Attività
- Configurare OpenAPI con schema JWT bearer.
- Aggiungere annotazioni endpoint e descrizioni payload.
- Inserire test integrati minimi (auth, ticket, analyze).
- Verificare logging e sanitizzazione dati sensibili.
- Eseguire smoke test end-to-end.

### Dipende da
- Fase 4.

### Deliverable
- Build documentata e verificata.

### Definition of Done
- Swagger navigabile con autenticazione.
- Test minimi verdi su pipeline locale.

---

## 5) Sequenza operativa consigliata (anti-rework)

1. Chiudere Fase 0 prima di scrivere logica applicativa.
2. Bloccare schema dati in Fase 1.
3. Implementare sicurezza completa in Fase 2.
4. Esporre endpoint ticket in Fase 3.
5. Integrare AI in Fase 4 solo con contratti già stabili.
6. Finalizzare quality gate in Fase 5.

Motivo: ogni fase riduce incertezza per la successiva, minimizzando rifattorizzazioni trasversali.

---

## 6) Checklist trasversale per ogni fase

- Contratti DTO definiti prima del controller.
- Regole di autorizzazione esplicite e testate.
- Errori standardizzati.
- Aggiornamento documentazione (`docs/pipeline.md`, `docs/handoff.md`).
- Commit atomico per fase/sottofase.

---

## 7) Primo sprint consigliato (immediato)

### Sprint target
Chiudere Fase 0 + base Fase 1.

### Task eseguibili subito
- Build Maven e fix eventuali incompatibilita dipendenze.
- Creazione classi entity/repository minime.
- Test persistenza di base con DB locale.

### Exit criteria sprint
- App avviabile.
- Tabelle `users` e `tickets` presenti e consistenti.
- Base pronta per implementare JWT senza rework su persistenza.