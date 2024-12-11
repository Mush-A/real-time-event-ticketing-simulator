package com.realtimeeventticketing.core.tickets;

import com.realtimeeventticketing.core.simulation.SimulationConfig;
import com.realtimeeventticketing.core.users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages a pool of tickets for the simulation, handling ticket production and consumption.
 */
public class TicketPool {
    private final SimulationConfig simulationConfig;
    private final List<Ticket> tickets;
    private final List<TicketEvent> eventStore;
    private final Lock lock;
    private final Condition condition;
    private final List<ITicketPoolObserver> observers;
    private int totalTickets;
    private int maxTicketsCapacity;
    private int soldTicketsCount = 0;
    private int producedTicketsCount = 0;
    private boolean isProductionOver = false;

    /**
     * Constructs a TicketPool with the specified simulation configuration.
     *
     * @param config the simulation configuration
     */
    public TicketPool(SimulationConfig config) {
        this.simulationConfig = config;
        this.totalTickets = config.getTotalTickets();
        this.maxTicketsCapacity = config.getMaxTicketsCapacity();
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.eventStore = Collections.synchronizedList(new ArrayList<>());
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.observers = new ArrayList<>();
    }

    /**
     * Adds an observer to the ticket pool.
     *
     * @param observer the observer to add
     */
    public void addObserver(ITicketPoolObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers of a ticket event.
     *
     * @param event the ticket event
     * @throws InterruptedException if the thread is interrupted
     */
    private void notifyObservers(TicketEvent event) throws InterruptedException {
        for (ITicketPoolObserver observer : observers) {
            observer.onTicketEvent(event);
        }
    }

    /**
     * Adds a ticket to the pool.
     *
     * @param user the user adding the ticket
     * @throws InterruptedException if the thread is interrupted
     */
    public void addTicket(User user) throws InterruptedException {
        lock.lock();
        Ticket ticket = null;
        try {
            if (!user.isRunning() || isProductionOver) return;
            if (producedTicketsCount >= totalTickets) {
                String message = "All tickets have been produced. No more tickets can be added.";
                TicketEvent event = new TicketEvent(TicketEventType.PRODUCTION_OVER, message, simulationConfig);
                this.notifyObservers(event);
                this.eventStore.add(event);
                isProductionOver = true;
                return;
            }
            if (tickets.size() == maxTicketsCapacity) {
                String message = "Ticket pool is full. Cannot add more tickets. Waiting for customers to buy tickets.";
                TicketEvent event = new TicketEvent(TicketEventType.POOL_FULL, message, simulationConfig);
                this.notifyObservers(event);
                this.eventStore.add(event);
                condition.await();
            } else {
                ticket = new Ticket(100);
                tickets.add(ticket);
                producedTicketsCount++;
                String message = "Ticket " + ticket.getId() + " added to the pool by " + user.getName();
                TicketEvent event = new TicketEvent(TicketEventType.TICKET_ADDED, message, user, ticket, simulationConfig);
                this.notifyObservers(event);
                this.eventStore.add(event);
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes a ticket from the pool.
     *
     * @param user the user removing the ticket
     * @throws InterruptedException if the thread is interrupted
     */
    public void removeTicket(User user) throws InterruptedException {
        lock.lock();
        Ticket ticket = null;
        if (!user.isRunning()) return;
        try {
            if (tickets.isEmpty()) {
                String message = "Ticket pool is empty. Cannot remove tickets. Waiting for vendors to add tickets.";
                TicketEvent event = new TicketEvent(TicketEventType.POOL_EMPTY, message, simulationConfig);
                this.notifyObservers(event);
                this.eventStore.add(event);
                condition.await();
            } else {
                ticket = tickets.remove(0).buyTicket();
                soldTicketsCount++;
                String message = "Ticket " + ticket.getId() + " purchased by " + user.getName();
                TicketEvent event = new TicketEvent(TicketEventType.TICKET_PURCHASED, message, user, ticket, simulationConfig);
                this.notifyObservers(event);
                this.eventStore.add(event);
                condition.signalAll();

                if (soldTicketsCount >= totalTickets) {
                    String simulationOverMessage = "All tickets have been sold. Simulation is over.";
                    TicketEvent simulationOverEvent = new TicketEvent(TicketEventType.SIMULATION_OVER, simulationOverMessage, simulationConfig);
                    this.notifyObservers(simulationOverEvent);
                    this.eventStore.add(simulationOverEvent);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Stops all threads waiting on the condition.
     */
    public void stopAllWaiting() {
        lock.lock();
        try {
            // Wake up all threads waiting on condition
            condition.signalAll();
        } finally {
            lock.unlock();
            Ticket.resetId();
            this.eventStore.clear();
        }
    }

    /**
     * Sets the maximum tickets capacity.
     *
     * @param maxTicketsCapacity the maximum tickets capacity
     */
    public void setMaxTicketsCapacity(int maxTicketsCapacity) {
        lock.lock();
        try {
            this.maxTicketsCapacity = maxTicketsCapacity;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the total number of tickets.
     *
     * @param totalTickets the total number of tickets
     */
    public void setTotalTickets(int totalTickets) {
        lock.lock();
        try {
            this.totalTickets = totalTickets;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the event store containing all ticket events.
     *
     * @return the event store
     */
    public List<TicketEvent> getEventStore() {
        return eventStore;
    }
}