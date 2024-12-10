package com.realtimeeventticketing.core.users;

import com.realtimeeventticketing.core.tickets.TicketPool;

public class Vendor extends User {

    public static int sequence = 0;
    private final TicketPool ticketPool;

    public Vendor(TicketPool ticketPool, int rate) {
        super(sequence++, "Vendor " + sequence, rate);
        super.type = UserType.VENDOR;
        this.ticketPool = ticketPool;
    }

    public static void resetId() {
        sequence = 0;
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                ticketPool.addTicket(this);
                super.addTicket();
                Thread.sleep(getRate());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
