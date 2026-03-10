# WhosUp

A full-stack web application for finding local activity partners. Post activities like "Watch Zootopia 2 at AMC Empire 25" or "Morning tennis at Central Park" and find people to join you.

## Architecture

```text
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    Frontend     │     │    Backend      │     │   PostgreSQL    │
│  React + Vite   │────▶│  Spring Boot    │────▶│                 │
│  Port 5173      │     │  Port 8080      │     │   Port 5432     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
       MUI                   Spring Security
   TanStack Query            JWT Auth
   React Hook Form           Spring Data JPA
   React Router              Flyway Migrations
```

### API Design

| Method | Endpoint                       | Description                  | Auth |
| ------ | ------------------------------ | ---------------------------- | ---- |
| POST   | `/api/auth/register`           | Register                     | No   |
| POST   | `/api/auth/login`              | Login                        | No   |
| GET    | `/api/auth/me`                 | Current user                 | Yes  |
| GET    | `/api/activities`              | List (paginated, filterable) | No   |
| GET    | `/api/activities/:id`          | Detail with participants     | No   |
| POST   | `/api/activities`              | Create                       | Yes  |
| PUT    | `/api/activities/:id`          | Update (creator)             | Yes  |
| DELETE | `/api/activities/:id`          | Delete (creator)             | Yes  |
| PATCH  | `/api/activities/:id/cancel`   | Cancel (creator)             | Yes  |
| PATCH  | `/api/activities/:id/complete` | Complete (creator)           | Yes  |
| POST   | `/api/activities/:id/join`     | Join                         | Yes  |
| DELETE | `/api/activities/:id/leave`    | Leave                        | Yes  |
| GET    | `/api/users/me/activities`     | My joined activities         | Yes  |
| GET    | `/api/users/me/created`        | My created activities        | Yes  |

### Data Model

```text
users ───────────┐
  id             │
  email          │   activities
  password       │     id
  display_name   │     title, description, location
  created_at     │     activity_date, category, status
                 ├────▶creator_id (FK)
                 │     max_participants
                 │
                 │   participations
                 │     id
                 └────▶user_id (FK)
                       activity_id (FK)
                       joined_at
```

## Tech Stack

**Backend:** Java 21, Spring Boot 3.5, Spring Security + JWT, Spring Data JPA, PostgreSQL, Flyway, SpringDoc OpenAPI

**Frontend:** TypeScript, React 18, Vite, MUI, React Router, TanStack Query, React Hook Form + Zod, Axios

**DevOps:** Docker Compose, GitHub Actions CI/CD, multi-arch Docker images (amd64/arm64) on GHCR, Kubernetes (K3s)

## Getting Started

### Prerequisites

- Java 21
- Node.js 20+
- Docker

### Quick Start

```bash
# Start the database
docker compose up -d db

# Start the backend
cd backend
./gradlew bootRun

# In another terminal, start the frontend
cd frontend
npm install
npm run dev
```

The app is available at <http://localhost:5173>.

### Demo Account

Click **"Try Demo Account"** on the login page to instantly log in with a pre-seeded account. The database comes with sample activities across different categories.

- Email: `demo@whosup.com`
- Password: `demo1234`

### Run with Docker (full stack)

```bash
docker compose up --build
```

The app is available at <http://localhost:3000>.

### Running Tests

```bash
# Backend
cd backend
./gradlew test

# Frontend
cd frontend
npx tsc --noEmit
```

### Deploy to Kubernetes (K3s)

Pre-built multi-arch images are published to GitHub Container Registry on every push to `main`.

```bash
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/postgres.yml
kubectl apply -f k8s/backend.yml
kubectl apply -f k8s/frontend.yml
kubectl apply -f k8s/ingress.yml
```

A daily CronJob restarts the backend to re-seed fresh demo data.

### API Documentation

With the backend running, visit <http://localhost:8080/swagger-ui.html> for interactive API docs.
