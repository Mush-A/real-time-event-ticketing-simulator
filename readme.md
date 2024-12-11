# Real-Time Event Ticketing System

## Overview

This project is a Real-Time Event Ticketing System designed with a modular and clean architecture. The system is divided into three main components: CLI, Core, and Web. Each component has a distinct role and responsibility, ensuring a clean separation of concerns and maintainability.

## Backend Architecture

### CLI

The CLI module provides a command-line interface for interacting with the system. It allows users to start, stop, and manage simulations directly from the terminal. This module is designed to be lightweight and easy to use.

### Core

The Core module contains the core logic of the system, implemented in pure Java. It is designed to be framework-agnostic, meaning it does not depend on any specific framework. This module includes the main simulation logic, ticket pool management, and the implementation of various design patterns such as the Observer and Builder patterns.

#### Observer Pattern

The Observer pattern is used to handle ticket events within the system. The `SimulationService` class implements the `ITicketPoolObserver` interface to receive updates about ticket events and take appropriate actions, such as stopping the simulation when it is over.

#### Builder Pattern

The Builder pattern is used to construct complex `Simulation` objects in a step-by-step manner. The `SimulationBuilder` class provides a fluent API for setting various simulation parameters and building the final `Simulation` object.

### Web

The Web module is built using Spring Boot and provides a web interface for interacting with the system. It includes RESTful APIs for managing simulations and WebSocket support for real-time updates. This module handles the persistence of simulation configurations using Spring Data JPA and communicates with the front-end using WebSocket messaging.

## Consumer-Producer Model

The ticket pool in the system follows a consumer-producer model. Vendors act as producers, releasing tickets into the pool, while customers act as consumers, retrieving tickets from the pool. This model ensures a dynamic and real-time flow of tickets within the system.

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle

### Building the Project

To build the project, run the following command:

```sh
./gradlew build
```

### Running the Application

To start the application, run the following command:

```sh
./gradlew bootRun
```

### Accessing the Web Interface

Once the application is running, you can access the web interface at `http://localhost:8080`.

## Frontend Architecture

The frontend of the Real-Time Event Ticketing System is built using Angular.

### Key Features

- **Real-Time Updates**: Utilizes WebSocket connections to receive real-time updates about ticket events and simulation status.
- **Responsive Design**: Ensures that the application usable in various desktop screens.
- **Modular Structure**: The frontend code is organized into modules, components, and services to maintain a clean and scalable architecture.

## Running the Frontend

### Prerequisites
- Node JS LTS

To run the frontend application, navigate to the `/fe-client` directory and use the following commands:

1. Install the dependencies:

    ```sh
    npm install
    ```

2. Start the development server:

    ```sh
    ng serve
    ```

3. Access the frontend application at `http://localhost:4200`.
