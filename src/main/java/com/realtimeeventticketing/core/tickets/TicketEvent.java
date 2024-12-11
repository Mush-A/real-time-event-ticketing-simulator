package com.realtimeeventticketing.core.tickets;

import com.realtimeeventticketing.core.simulation.SimulationConfig;
import com.realtimeeventticketing.core.users.User;

/**
 * Represents an event related to a ticket in the simulation.
 */
public class TicketEvent {
    /**
     * The type of the ticket event.
     */
    private final TicketEventType eventType;

    /**
     * The message associated with the ticket event.
     */
    private final String message;

    /**
     * The user associated with the ticket event.
     */
    private User user;

    /**
     * The ticket associated with the ticket event.
     */
    private Ticket ticket;

    /**
     * The simulation configuration associated with the ticket event.
     */
    private SimulationConfig simulationConfig;

    /**
     * Constructs a TicketEvent with the specified event type, message, and simulation configuration.
     *
     * @param eventType the type of the ticket event
     * @param message the message associated with the ticket event
     * @param simulationConfig the simulation configuration associated with the ticket event
     */
    public TicketEvent(TicketEventType eventType, String message, SimulationConfig simulationConfig) {
        this.eventType = eventType;
        this.message = message;
        this.simulationConfig = simulationConfig;
    }

    /**
     * Constructs a TicketEvent with the specified event type, message, user, ticket, and simulation configuration.
     *
     * @param eventType the type of the ticket event
     * @param message the message associated with the ticket event
     * @param user the user associated with the ticket event
     * @param ticket the ticket associated with the ticket event
     * @param simulationConfig the simulation configuration associated with the ticket event
     */
    public TicketEvent(TicketEventType eventType, String message, User user, Ticket ticket, SimulationConfig simulationConfig) {
        this.eventType = eventType;
        this.message = message;
        this.user = user;
        this.ticket = ticket;
        this.simulationConfig = simulationConfig;
    }

    /**
     * Gets the type of the ticket event.
     *
     * @return the type of the ticket event
     */
    public TicketEventType getEventType() {
        return eventType;
    }

    /**
     * Gets the message associated with the ticket event.
     *
     * @return the message associated with the ticket event
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the user associated with the ticket event.
     *
     * @return the user associated with the ticket event
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the ticket associated with the ticket event.
     *
     * @return the ticket associated with the ticket event
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Gets the simulation configuration associated with the ticket event.
     *
     * @return the simulation configuration associated with the ticket event
     */
    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }
}