package com.realtimeeventticketing.tickets;

public interface ITicketPoolObserver {
    void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException;
}
