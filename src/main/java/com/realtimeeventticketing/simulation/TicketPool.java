package com.realtimeeventticketing.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final List<Ticket> tickets;
    private final Lock lock;
    private final Condition condition;
    private int totalTickets;
    private int maxTicketsCapacity;
    private int soldTicketsCount = 0;
    private int producedTicketsCount = 0;
    private boolean isProductionOver = false;

    private final List<ITicketPoolObserver> observers;

    public void addObserver(ITicketPoolObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ITicketPoolObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(TicketEvent event) throws InterruptedException {
        for (ITicketPoolObserver observer : observers) {
            observer.onTicketEvent(event);
        }
    }

    public TicketPool(int totalTickets, int maxTicketsCapacity) {
        this.totalTickets = totalTickets;
        this.maxTicketsCapacity = maxTicketsCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.observers = new ArrayList<>();
    }

    public Ticket addTicket(User user) throws InterruptedException {
        lock.lock();
        Ticket ticket = null;
        try {
            if (!user.isRunning() || isProductionOver) return null;
            if (producedTicketsCount >= totalTickets) {
                String message = "All tickets have been produced. No more tickets can be added.";
                System.out.println(message);
                this.notifyObservers(new TicketEvent(
                        TicketEventType.PRODUCTION_OVER,
                        message
                ));
                isProductionOver = true;
                return null;
            }
            if (tickets.size() == maxTicketsCapacity) {
                String message = "Ticket pool is full. Cannot add more tickets. Waiting for customers to buy tickets.";
                System.out.println(message);
                this.notifyObservers(new TicketEvent(
                        TicketEventType.POOL_FULL,
                        message
                ));
                condition.await();
            } else {
                ticket = new Ticket(100);
                tickets.add(ticket);
                producedTicketsCount++;
                String message = "Ticket added to the pool." + ticket;
                System.out.println(message);
                this.notifyObservers(new TicketEvent(
                        TicketEventType.TICKET_ADDED,
                        message,
                        user,
                        ticket
                ));
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
        return ticket;
    }

    public Ticket removeTicket(User user) throws InterruptedException {
        lock.lock();
        Ticket ticket = null;
        if (!user.isRunning()) return null;
        try {
            if (tickets.isEmpty()) {
                String message = "Ticket pool is empty. Cannot remove tickets. Waiting for vendors to add tickets.";
                System.out.println(message);
                this.notifyObservers(new TicketEvent(
                        TicketEventType.POOL_EMPTY,
                        message
                ));
                condition.await();
            } else {
                ticket = tickets.remove(0).buyTicket();
                soldTicketsCount++; // Increment the sold tickets count
                String message = "Ticket removed from the pool." + ticket;
                System.out.println(message);
                this.notifyObservers(new TicketEvent(
                        TicketEventType.TICKET_PURCHASED,
                        message,
                        user,
                        ticket
                ));
                condition.signalAll();

                if (soldTicketsCount >= totalTickets) {
                    String simulationOverMessage = "All tickets have been sold. Simulation is over.";
                    System.out.println(simulationOverMessage);
                    this.notifyObservers(new TicketEvent(
                            TicketEventType.SIMULATION_OVER,
                            simulationOverMessage
                    ));
                }
            }
        } finally {
            lock.unlock();
        }
        return ticket;
    }

    public void stopAllWaiting() {
        lock.lock();
        try {
            // Wake up all threads waiting on condition
            condition.signalAll();
        } finally {
            lock.unlock();
            Ticket.resetId();
        }
    }

    public void setMaxTicketsCapacity(int maxTicketsCapacity) {
        lock.lock();
        try {
            this.maxTicketsCapacity = maxTicketsCapacity;
            condition.signalAll(); // Notify all waiting threads in case the new capacity allows for more tickets
        } finally {
            lock.unlock();
        }
    }

    public void setTotalTickets(int totalTickets) {
        lock.lock();
        try {
            this.totalTickets = totalTickets;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
