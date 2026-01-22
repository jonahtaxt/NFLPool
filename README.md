# NFLPool — Learning Project (Workflows & State)

NFLPool is a **learning-focused, full-stack side project** built to explore
application workflows, state handling, and incremental feature evolution
within a constrained domain.

The intent of this project was **not** to build a production-grade system,
but to practice designing, evolving, and reasoning about an end-to-end
application: backend APIs, data modeling, frontend interaction, and deployment.

---

## Why This Project Exists

This project was used to explore:

- Modeling **stateful workflows** (weekly predictions → scoring → rankings)
- Managing **durable historical data** across seasons and weeks
- Designing clear **API boundaries** between frontend and backend
- Iterating on features without breaking existing behavior
- Making pragmatic tradeoffs appropriate for a small, single-instance system

The NFL pool domain provided a familiar problem space with clear rules,
predictable cycles, and well-defined correctness requirements.

---

## What the System Does (High Level)

- Accepts weekly game predictions
- Retrieves game results
- Calculates scores and rankings
- Persists results by season and week
- Displays aggregated results via a web UI

---

## Architecture Overview

React (Vite) ──▶ Spring Boot API ──▶ Couchbase
│
└──▶ External data source (score retrieval)


This structure was intentionally chosen to practice:
- Separation of concerns
- Stateless API design
- Document-oriented data modeling

---

## Tech Stack (Learning Context)

**Backend**
- Java 21
- Spring Boot
- Couchbase
- JSoup (HTML parsing)
- Apache POI (Excel ingestion)

**Frontend**
- React
- Vite
- Material UI

**Infrastructure**
- Docker
- Docker Compose
- Nginx

---

## Light Real-World Usage

Although NFLPool was built as a learning project, it was used informally by
~70 participants across multiple weeks.

This provided an opportunity to:
- Incorporate real user feedback
- Address edge cases in scoring and workflows
- Validate data modeling choices under actual usage
- Practice iterating safely without over-engineering

The system was intentionally kept simple and was not treated as a
production-grade service.

---

## What This Project Is *Not*

- ❌ Not a production system
- ❌ Not designed for large-scale or multi-tenant use
- ❌ Not intended to demonstrate advanced security or availability patterns

Those concerns are intentionally explored in other projects.

---

## What I’d Do Differently at Scale

If this system were designed for real-world scale, I would:
- Replace scraping with a stable ingestion pipeline
- Introduce idempotency and retries around result ingestion
- Version scoring rules explicitly
- Separate write-heavy and read-heavy workloads
- Add stronger consistency guarantees for ranking calculations

---

## Status

This project is **complete as a learning exercise** and is not under active development.
