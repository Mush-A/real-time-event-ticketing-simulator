package com.realtimeeventticketing.entities;

import com.realtimeeventticketing.TicketPool;

public class Customer implements Runnable {

    private int id;
    private String name;
    private TicketPool ticketPool;
    private volatile boolean running = true;

    public Customer(String name, TicketPool ticketPool) {
        this.id = (int) (Math.random() * 1000);
        this.name = name;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (running) {
            try {
                ticketPool.removeTicket();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
