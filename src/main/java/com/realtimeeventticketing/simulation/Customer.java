package com.realtimeeventticketing.simulation;

public class Customer extends User {

    private final TicketPool ticketPool;

    public static int sequence = 0;

    public Customer(String name, TicketPool ticketPool, int rate) {
        super(sequence++, name + sequence, rate);
        super.type = UserType.CUSTOMER;
        this.ticketPool = ticketPool;
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

    public static void resetId() {
        sequence = 0;
    }
}
