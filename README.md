![CI](https://github.com/Kaushik463/bookmyshow/actions/workflows/ci.yml/badge.svg)

# BookMyShow — Movie Ticket Booking System

A backend movie ticket booking system built with Spring Boot, demonstrating production-style patterns: JWT authentication, role-based access control, concurrency-safe seat booking, automated testing, containerization, and CI.

## Features

- **Auth**: JWT-based registration/login with role-based access (`USER`, `ADMIN`)
- **Movie / Theatre / Screen / Show management**: full CRUD for admins, public browsing for everyone
- **Seat booking with concurrency control**: pessimistic row-level locking (`PESSIMISTIC_WRITE`) ensures two users can never double-book the same seat, even under simultaneous requests
- **Booking lifecycle**: hold seats → pay → confirm, with a scheduled job that auto-releases abandoned holds after 5 minutes
- **Global exception handling**: consistent JSON error responses with correct HTTP status codes
- **API documentation**: interactive Swagger UI
- **Automated tests**: unit tests (Mockito) + a Testcontainers integration test that fires 10 concurrent threads at the same seat to prove exactly one booking succeeds
- **Containerized**: multi-stage Dockerfile + docker-compose (app + MySQL)
- **CI**: GitHub Actions runs the full build and test suite on every push

## Tech Stack

Java 17 · Spring Boot · Spring Security (JWT) · Spring Data JPA · Hibernate · MySQL · Flyway · MapStruct · Lombok · JUnit 5 · Mockito · Testcontainers · Docker · GitHub Actions · Maven

## Architecture