package com.realtimeeventticketing.users;

import com.realtimeeventticketing.tickets.TicketPool;

public class Customer extends User {

    public static int sequence = 0;
    private final TicketPool ticketPool;

    public Customer(String name, TicketPool ticketPool, int rate) {
        super(sequence++, name + sequence, rate);
        super.type = UserType.CUSTOMER;
        this.ticketPool = ticketPool;
    }

    public static void resetId() {
        sequence = 0;
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                ticketPool.removeTicket(this);
                super.addTicket();
                Thread.sleep(getRate());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
