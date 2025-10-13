# Spring AI Hotel Booking - AI-Powered Hotel Booking Application

A full-stack hotel booking application using Spring Boot (Java) for the backend and Angular (TypeScript) for the frontend. Integrates AI-powered chat and document retrieval using Azure OpenAI and PGVector.

## Features

- AI-powered chat assistant for guests
- RAG (Retrieval-Augmented Generation) with vector search (PGVector) for Terms of Service
- MCP with direct integration with booking tools for real-time operations
- Memory Management for persistent conversation history across chat sessions

## Technologies

- Java, Spring Boot, Spring AI
- Angular, TypeScript, SCSS
- PostgreSQL, PGVector
- Azure OpenAI (GPT, Embeddings)
- Docker Compose

## Prerequisites

- Java 24+
- Node.js 18+ & npm
- Maven
- Docker & Docker Compose
- Azure OpenAI API key

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ERNI-Academy/spring-ai-hotel-booking.git
cd spring-ai-hotel-booking
```

### 2. Backend Setup

- Configure environment variables:
    - `AZURE_OPENAI_API_KEY`
    - `DB_PASSWORD`
- Start PostgreSQL (or use Docker Compose):

```bash
docker compose up -d
```

- Build and run the backend:

```bash
./mvnw spring-boot:run
```

### 3. Frontend Setup

```bash
cd frontend
npm install
npm start
```

The Angular app will be available at `http://localhost:4200`.

### 4. API Endpoints

- Swagger/OpenAPI: `http://localhost:8080/swagger-ui.html`
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

## Configuration

Edit `src/main/resources/application.yml` for backend settings (database, AI, logging, etc.).

## Project Structure

```
spring-ai-hotel-booking/
├── src/main/java/com/erni/springaihotel/
│   ├── controller/          # REST controllers
│   ├── service/             # Business logic and AI services
│   └── model/               # Data models
├── src/main/resources/
│   ├── application.yml      # Application configuration
│   └── rag/                 # RAG documents
├── frontend/
│   ├── src/app/
│   │   ├── components/      # Angular components
│   │   ├── services/        # Angular services
│   │   └── types/           # TypeScript interfaces
│   └── package.json
├── compose.yaml             # Docker Compose configuration
└── pom.xml                  # Maven configuration
```

## License

See [LICENSE](LICENSE).

---

**Author:** [raffaele-auriemma](https://github.com/raffaele-auriemma)
