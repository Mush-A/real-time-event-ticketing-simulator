package com.realtimeeventticketing;

import com.realtimeeventticketing.entities.Ticket;

public class TicketEvent {
    private final EventType eventType;
    private final String message;
    private String customerName;
    private String vendorName;
    private Ticket ticket;

    // Constructor
    public TicketEvent(EventType eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }

    public TicketEvent(EventType eventType, String message, String customerName, String vendorName, Ticket ticket) {
        this.eventType = eventType;
        this.message = message;
        this.customerName = customerName;
        this.vendorName = vendorName;
        this.ticket = ticket;
    }

    // Getters and Setters
    public EventType getEventType() { return eventType; }
    public String getMessage() { return message; }
    public String getCustomerName() { return customerName; }
    public String getVendorName() { return vendorName; }
    public Ticket getTicket() { return ticket; }
}

