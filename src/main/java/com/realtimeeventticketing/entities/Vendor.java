package com.realtimeeventticketing.entities;

import com.realtimeeventticketing.TicketPool;

public class Vendor extends User {

    private final TicketPool ticketPool;

    public Vendor(String name, TicketPool ticketPool, int rate) {
        super(0, name, rate);
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                ticketPool.addTicket(this);
                Thread.sleep(getRate());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
