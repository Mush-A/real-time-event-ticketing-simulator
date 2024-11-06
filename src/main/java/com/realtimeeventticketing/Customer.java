package com.realtimeeventticketing;

public class Customer extends User {

    private final TicketPool ticketPool;

    public Customer(String name, TicketPool ticketPool, int rate) {
        super(0, name, rate);
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                ticketPool.removeTicket(this);
                Thread.sleep(getRate());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
