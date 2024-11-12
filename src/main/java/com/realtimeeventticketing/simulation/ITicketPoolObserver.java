package com.realtimeeventticketing.simulation;

public interface ITicketPoolObserver {
    void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException;
}
