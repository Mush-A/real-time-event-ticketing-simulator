package com.realtimeeventticketing;

public class Ticket {

    private final int id;
    private TicketStatus status;
    private final double price;

    public static int sequence = 0;

    public Ticket(double price) {
        id = sequence++;
        this.price = price;
        this.status = TicketStatus.AVAILABLE;
    }

    public Ticket buyTicket(){
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
