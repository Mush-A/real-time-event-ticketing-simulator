package com.realtimeeventticketing.core.tickets;

/**
 * Interface for observing ticket pool events.
 */
public interface ITicketPoolObserver {
    /**
     * Called when a ticket event occurs.
     *
     * @param ticketEvent the ticket event
     * @throws InterruptedException if the thread is interrupted
     */
    void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException;
}