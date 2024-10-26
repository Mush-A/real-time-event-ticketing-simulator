package com.realtimeeventticketing;

import com.realtimeeventticketing.entities.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final int customerRetrievalRate;
    private final int maxTicketsCapacity;
    private final List<Ticket> tickets;
    private final Lock lock;
    private final Condition condition;

    public TicketPool(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketsCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketsCapacity = maxTicketsCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void addTicket() throws InterruptedException {
        lock.lock();
        try {
            if (tickets.size() == maxTicketsCapacity) {
                System.out.println("Ticket pool is full. Cannot add more tickets. Waiting for customers to buy tickets.");
                condition.await();
            } else {
                Ticket ticket = new Ticket(100);
                tickets.add(ticket);
                System.out.println("Ticket added to the pool." + ticket);
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
        Thread.sleep(ticketReleaseRate);
    }

    public void removeTicket() throws InterruptedException {
        lock.lock();
        try {
            if (tickets.isEmpty()) {
                System.out.println("Ticket pool is empty. Cannot remove tickets. Waiting for vendors to add tickets.");
                condition.await();
            } else {
                Ticket ticket = tickets.remove(0).buyTicket();
                System.out.println("Ticket removed from the pool." + ticket);
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
        Thread.sleep(customerRetrievalRate);
    }
}
