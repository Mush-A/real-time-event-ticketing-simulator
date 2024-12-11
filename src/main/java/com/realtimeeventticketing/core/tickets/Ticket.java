package com.realtimeeventticketing.core.tickets;

/**
 * Represents a ticket with a unique ID, price, and status.
 */
public class Ticket {

    /**
     * Sequence number for generating unique ticket IDs.
     */
    public static int sequence = 1;

    /**
     * Unique ID of the ticket.
     */
    private final int id;

    /**
     * Price of the ticket.
     */
    private final double price;

    /**
     * Status of the ticket.
     */
    private TicketStatus status;

    /**
     * Constructs a Ticket with the specified price and sets its status to AVAILABLE.
     *
     * @param price the price of the ticket
     */
    public Ticket(double price) {
        id = sequence++;
        this.price = price;
        this.status = TicketStatus.AVAILABLE;
    }

    /**
     * Resets the ticket ID sequence to 1.
     */
    public static void resetId() {
        sequence = 1;
    }

    /**
     * Buys the ticket by setting its status to SOLD.
     *
     * @return the current Ticket instance with updated status
     */
    public Ticket buyTicket() {
        this.status = TicketStatus.SOLD;
        return this;
    }

    /**
     * Gets the unique ID of the ticket.
     *
     * @return the unique ID of the ticket
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the status of the ticket.
     *
     * @return the status of the ticket
     */
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * Gets the price of the ticket.
     *
     * @return the price of the ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns a string representation of the ticket.
     *
     * @return a string representation of the ticket
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}