package com.realtimeeventticketing.core.tickets;

public class Ticket {

    public static int sequence = 1;
    private final int id;
    private final double price;
    private TicketStatus status;

    public Ticket(double price) {
        id = sequence++;
        this.price = price;
        this.status = TicketStatus.AVAILABLE;
    }

    public static void resetId() {
        sequence = 1;
    }

    public Ticket buyTicket() {
        this.status = TicketStatus.SOLD;
        return this;
    }

    public int getId() {
        return id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
