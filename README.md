# TNPSC Daily Practice Platform

A premium mobile-first TNPSC preparation platform focused on daily practice, streaks, bilingual support, and smooth engagement.

## Architecture

- Frontend: React + Vite + React Router + Axios
- Backend: Spring Boot + Spring Security + JWT + Hibernate/JPA
- Database: PostgreSQL

## Features

- JWT authentication
- Daily 10-question practice flow
- Tamil/English bilingual support
- Streak tracking and habit formation
- Basic analytics with accuracy and weak-topic insights
- Premium dark mobile-first UI

## Run locally

### Backend
1. Configure PostgreSQL credentials in `backend/src/main/resources/application.yml`
2. Run from IDE or `./mvnw spring-boot:run`

### Frontend
1. `cd frontend`
2. `npm install`
3. `npm run dev`

## Notes

The backend seeds a high-quality initial set of TNPSC polity questions on startup using `import.sql`.

## Upcoming Features

- Mock tests
- Current affairs dashboard
- AI analytics

Project Status: Feature B version
