package com.realtimeeventticketing.simulation;

public class TicketEvent {
    private final TicketEventType eventType;
    private final String message;
    private User user;
    private Ticket ticket;

    // Constructor
    public TicketEvent(TicketEventType eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }

    public TicketEvent(TicketEventType eventType, String message, User user, Ticket ticket) {
        this.eventType = eventType;
        this.message = message;
        this.user = user;
        this.ticket = ticket;
    }

    // Getters and Setters
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
}

