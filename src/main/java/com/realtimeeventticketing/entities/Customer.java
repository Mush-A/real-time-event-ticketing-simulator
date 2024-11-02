package com.realtimeeventticketing.entities;

import com.realtimeeventticketing.TicketPool;

public class Customer extends User {

    private final TicketPool ticketPool;

    public Customer(String name, TicketPool ticketPool) {
        super(0, name);
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (running) {
            try {
                ticketPool.removeTicket(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
