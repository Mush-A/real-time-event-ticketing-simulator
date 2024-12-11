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
- Node.js and npm (for front-end development)

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

The frontend of the Real-Time Event Ticketing System is built using Angular, a popular framework for building web applications. The frontend communicates with the backend services to provide a seamless user experience for managing and interacting with simulations.

### Key Features

- **Real-Time Updates**: Utilizes WebSocket connections to receive real-time updates about ticket events and simulation status.
- **Responsive Design**: Ensures that the application is accessible and usable on various devices, including desktops, tablets, and mobile phones.
- **Modular Structure**: The frontend code is organized into modules, components, and services to maintain a clean and scalable architecture.

### Directory Structure

The `/fe-client` directory contains the following key folders and files:

- **src/app**: Contains the main application code, including modules, components, and services.
    - **components**: Houses the Angular components used in the application, such as the simulation dashboard, ticket event list, and configuration forms.
    - **services**: Contains the services responsible for making HTTP requests to the backend and managing WebSocket connections.
    - **models**: Defines the TypeScript interfaces and classes used throughout the application.
    - **modules**: Organizes the application into feature modules for better maintainability and scalability.
- **assets**: Contains static assets such as images, stylesheets, and fonts.
- **environments**: Holds the environment configuration files for different build environments (e.g., development, production).

### Key Components

- **SimulationDashboardComponent**: Displays the current status of the simulation, including active ticket events and simulation parameters.
- **TicketEventListComponent**: Shows a list of ticket events in real-time, updating as new events occur.
- **SimulationConfigFormComponent**: Provides a form for creating and updating simulation configurations.

### Running the Frontend

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