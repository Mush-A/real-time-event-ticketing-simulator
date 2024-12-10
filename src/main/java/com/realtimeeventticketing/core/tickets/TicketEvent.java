package com.realtimeeventticketing.core.tickets;

import com.realtimeeventticketing.core.simulation.SimulationConfig;
import com.realtimeeventticketing.core.users.User;

public class TicketEvent {
    private final TicketEventType eventType;
    private final String message;
    private User user;
    private Ticket ticket;
    private SimulationConfig simulationConfig;

    public TicketEvent(TicketEventType eventType, String message, SimulationConfig simulationConfig) {
        this.eventType = eventType;
        this.message = message;
        this.simulationConfig = simulationConfig;
    }

    public TicketEvent(TicketEventType eventType, String message, User user, Ticket ticket, SimulationConfig simulationConfig) {
        this.eventType = eventType;
        this.message = message;
        this.user = user;
        this.ticket = ticket;
        this.simulationConfig = simulationConfig;
    }

    public TicketEventType getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }
}

