package com.realtimeeventticketing.simulation;

public class TicketEvent {
    private final TicketEventType eventType;
    private final String message;
    private String customerName;
    private String vendorName;
    private Ticket ticket;

    // Constructor
    public TicketEvent(TicketEventType eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }

    public TicketEvent(TicketEventType eventType, String message, String customerName, String vendorName, Ticket ticket) {
        this.eventType = eventType;
        this.message = message;
        this.customerName = customerName;
        this.vendorName = vendorName;
        this.ticket = ticket;
    }

    // Getters and Setters
    public TicketEventType getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public Ticket getTicket() {
        return ticket;
    }
}

