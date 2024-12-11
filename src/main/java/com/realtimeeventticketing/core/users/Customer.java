package com.realtimeeventticketing.core.users;

import com.realtimeeventticketing.core.tickets.TicketPool;

/**
 * Represents a customer in the ticketing system.
 */
public class Customer extends User {

    /**
     * Sequence number for generating unique customer IDs.
     */
    public static int sequence = 0;

    /**
     * The ticket pool associated with the customer.
     */
    private final TicketPool ticketPool;

    /**
     * Constructs a Customer with the specified ticket pool and rate.
     *
     * @param ticketPool the ticket pool associated with the customer
     * @param rate the rate at which the customer operates
     */
    public Customer(TicketPool ticketPool, int rate) {
        super(sequence++, "Customer " + sequence, rate);
        super.type = UserType.CUSTOMER;
        this.ticketPool = ticketPool;
    }

    /**
     * Resets the customer ID sequence to 0.
     */
    public static void resetId() {
        sequence = 0;
    }

    /**
     * Runs the customer thread, removing tickets from the pool and adding them to the customer.
     */
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