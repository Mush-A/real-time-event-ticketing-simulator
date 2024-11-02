package com.realtimeeventticketing.entities;

import com.realtimeeventticketing.TicketPool;

public class Vendor extends User {

    private final TicketPool ticketPool;

    public Vendor(String name, TicketPool ticketPool) {
        super(0, name);
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (running) {
            try {
                ticketPool.addTicket(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
