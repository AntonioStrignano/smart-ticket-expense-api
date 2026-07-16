# Smart Ticket & Expense API

## Descrizione del Progetto

L'applicazione è un'API REST backend sviluppata in Java con l'ecosistema Spring. Il sistema gestisce l'automazione dei flussi aziendali legati ai ticket di assistenza e alle note spese, integrando un livello di intelligenza artificiale per l'estrazione dati e la categorizzazione automatica di input non strutturati. L'architettura è completamente disaccoppiata, stateless e predisposta per essere consumata da client frontend moderni.

---

## Stack Tecnologico

| Componente | Tecnologia |
|---|---|
| Linguaggio | Java 21 (LTS) |
| Framework | Spring Boot 3.x |
| Sicurezza | Spring Security + JWT |
| Accesso ai Dati | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Integrazione AI | Spring AI 2.0.0 |
| Documentazione API | Springdoc-openapi (Swagger UI) |

---

## Architettura del Sistema

```
[Client / Postman]
       │ (Richieste HTTP JSON / Bearer Token JWT)
       ▼
[Spring Security Filter Chain]
       │
       ▼
[Controllers] ───> [Data Transfer Objects (DTO/Records)]
       │
       ▼
[Services Layer]
       ├───> [Spring Data JPA Repositories] ───> [PostgreSQL]
       │
       └───> [Spring AI ChatClient] ───> [Provider LLM (OpenAI/Ollama)]
```

---

## Modello Dati (Database Schema)

### Tabella: `users`

| Colonna | Tipo | Vincoli |
|---|---|---|
| `id` | BIGINT | Generated Always as Identity, Primary Key |
| `username` | VARCHAR | Unique, Not Null |
| `password` | VARCHAR | Not Null |
| `email` | VARCHAR | Unique, Not Null |
| `role` | VARCHAR | Not Null |

### Tabella: `tickets`

| Colonna | Tipo | Vincoli |
|---|---|---|
| `id` | BIGINT | Generated Always as Identity, Primary Key |
| `title` | VARCHAR | Not Null |
| `description` | VARCHAR(500) | Not Null |
| `category` | VARCHAR | Not Null |
| `priority` | VARCHAR | Not Null |
| `status` | VARCHAR | Not Null |
| `created_at` | TIMESTAMP | Not Null |
| `user_id` | BIGINT | Foreign Key → `users.id` |

---

## Endpoints API

### Autenticazione (Pubblici)

| Metodo | Endpoint | Descrizione | Payload (JSON) | Risposta (JSON) |
|---|---|---|---|---|
| `POST` | `/api/auth/register` | Registrazione nuovo utente | `username`, `password` | Dati utente creato |
| `POST` | `/api/auth/login` | Autenticazione | `username`, `password` | `token` (JWT) |

### Gestione Ticket (Protetti da JWT)

| Metodo | Endpoint | Descrizione | Ruoli |
|---|---|---|---|
| `GET` | `/api/tickets` | Lista dei ticket dell'utente loggato | `USER`, `ADMIN` |
| `POST` | `/api/tickets` | Creazione manuale di un ticket strutturato | `USER`, `ADMIN` |
| `GET` | `/api/tickets/{id}` | Dettaglio singolo ticket | `USER`, `ADMIN` |
| `DELETE` | `/api/tickets/{id}` | Eliminazione ticket | `ADMIN` |

### Automazione AI (Protetti da JWT)

| Metodo | Endpoint | Descrizione | Payload | Risposta |
|---|---|---|---|---|
| `POST` | `/api/tickets/analyze` | Analisi ed estrazione automatica tramite LLM | `{"rawText": "..."}` | `category`, `priority`, `summary` mappati in Record Java |

---

## Configurazione (`src/main/resources/application.properties`)

```properties
# Configurazione Server
server.port=8080

# Configurazione Database (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/smart_ticket_db
spring.datasource.username=postgres
spring.datasource.password=your_secure_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configurazione Spring AI (Esempio con OpenAI)
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.3

# Proprietà Sicurezza JWT
jwt.secret=your_super_secret_key_minimum_256_bits_length_here
jwt.expiration=86400000
```

---

## Requisiti di Implementazione Tecnica Obbligatori

### 1. Gestione dei Tipi via Java Records

Utilizzare esclusivamente i Java Records per la definizione dei DTO (Data Transfer Objects), garantendo l'immutabilità dei dati scambiati tra i livelli del software.

### 2. Strutturazione dell'Output AI (Structured Output)

> L'endpoint `/api/tickets/analyze` deve forzare il modello linguistico a rispondere seguendo uno schema JSON rigido. Non sono ammesse risposte testuali libere dall'AI.

L'integrazione deve utilizzare la classe `BeanOutputConverter` o il meccanismo nativo di Spring AI per mappare la risposta del modello direttamente all'interno di un Record Java:

```java
public record TicketAnalysisResult(
    String category,
    String priority,
    String summary
) {}
```

### 3. Sicurezza Stateless

La configurazione di Spring Security deve disabilitare la gestione della sessione HTTP standard (`SessionCreationPolicy.STATELESS`) e validare ogni richiesta tramite un filtro personalizzato (`JwtAuthenticationFilter`) che estrae e verifica l'algoritmo di firma del Bearer Token presente nell'header `Authorization`.

