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

    private SimulationController simulationController = null;

    public TicketPool(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketsCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketsCapacity = maxTicketsCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public TicketPool(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketsCapacity, SimulationController simulationController) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketsCapacity = maxTicketsCapacity;
        this.simulationController = simulationController;
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
                String message = "Ticket pool is full. Cannot add more tickets. Waiting for customers to buy tickets.";
                System.out.println(message);
                this.sendTicketPoolUpdate(message);
                condition.await();
            } else {
                Ticket ticket = new Ticket(100);
                tickets.add(ticket);
                String message = "Ticket added to the pool." + ticket;
                System.out.println(message);
                this.sendTicketPoolUpdate(message);
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
                String message = "Ticket pool is empty. Cannot remove tickets. Waiting for vendors to add tickets.";
                System.out.println(message);
                this.sendTicketPoolUpdate(message);
                condition.await();
            } else {
                Ticket ticket = tickets.remove(0).buyTicket();
                String message = "Ticket removed from the pool." + ticket;
                System.out.println(message);
                this.sendTicketPoolUpdate(message);
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
        Thread.sleep(customerRetrievalRate);
    }

    private void sendTicketPoolUpdate(String message) {
        if (simulationController != null) {
            simulationController.sendSimulationUpdate(message);
        }
    }
}
