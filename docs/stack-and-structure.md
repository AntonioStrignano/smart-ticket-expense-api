# Stack Tecnologico

| Componente | Tecnologia | Versione |
|---|---|---|
| Linguaggio | Java | 21 (LTS) |
| Framework | Spring Boot | 3.x |
| Sicurezza | Spring Security + JWT | — |
| Accesso ai Dati | Spring Data JPA + Hibernate | — |
| Database | PostgreSQL | — |
| Integrazione AI | Spring AI | 2.0.0 |
| Documentazione API | Springdoc-openapi (Swagger UI) | — |

---

# Dipendenze Spring Initializr

Da aggiungere al momento della generazione del progetto:

- `Spring Web`
- `Spring Security`
- `Spring Data JPA`
- `PostgreSQL Driver`
- `Spring AI OpenAI` (o Ollama)
- `Springdoc OpenAPI` (aggiunta manuale nel `pom.xml`)
- `jjwt` (JJWT — aggiunta manuale nel `pom.xml`)

---

# Struttura Package Prevista

```
com.example.smartticket
├── config/
│   ├── SecurityConfig.java
│   └── ApplicationConfig.java
├── controller/
│   ├── AuthController.java
│   └── TicketController.java
├── service/
│   ├── AuthService.java
│   ├── TicketService.java
│   └── AiService.java
├── repository/
│   ├── UserRepository.java
│   └── TicketRepository.java
├── entity/
│   ├── User.java
│   └── Ticket.java
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── TicketRequest.java
│   ├── TicketResponse.java
│   └── TicketAnalysisResult.java
└── security/
    ├── JwtService.java
    └── JwtAuthenticationFilter.java
```

---

# Schema Database

## Tabella `users`

| Colonna | Tipo | Vincoli |
|---|---|---|
| `id` | UUID | Primary Key |
| `username` | VARCHAR | Unique, Not Null |
| `password` | VARCHAR | Not Null (BCrypt) |
| `role` | VARCHAR | Not Null |

## Tabella `tickets`

| Colonna | Tipo | Vincoli |
|---|---|---|
| `id` | BIGINT | Generated Always as Identity, PK |
| `title` | VARCHAR | Not Null |
| `description` | TEXT | Not Null |
| `category` | VARCHAR | — |
| `priority` | VARCHAR | — |
| `status` | VARCHAR | Not Null |
| `created_at` | TIMESTAMP | Not Null |
| `user_id` | UUID | FK → `users.id` |
