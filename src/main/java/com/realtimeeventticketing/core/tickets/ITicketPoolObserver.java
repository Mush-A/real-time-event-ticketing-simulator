package com.realtimeeventticketing.core.tickets;

public interface ITicketPoolObserver {
    void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException;
}
