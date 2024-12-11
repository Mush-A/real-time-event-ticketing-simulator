package com.realtimeeventticketing.core.users;

import com.realtimeeventticketing.core.tickets.TicketPool;

/**
 * Represents a vendor in the ticketing system.
 */
public class Vendor extends User {

    /**
     * Sequence number for generating unique vendor IDs.
     */
    public static int sequence = 0;

    /**
     * The ticket pool associated with the vendor.
     */
    private final TicketPool ticketPool;

    /**
     * Constructs a Vendor with the specified ticket pool and rate.
     *
     * @param ticketPool the ticket pool associated with the vendor
     * @param rate the rate at which the vendor operates
     */
    public Vendor(TicketPool ticketPool, int rate) {
        super(sequence++, "Vendor " + sequence, rate);
        super.type = UserType.VENDOR;
        this.ticketPool = ticketPool;
    }

    /**
     * Resets the vendor ID sequence to 0.
     */
    public static void resetId() {
        sequence = 0;
    }

    /**
     * Runs the vendor thread, adding tickets to the pool.
     */
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