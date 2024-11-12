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
    private final int totalTickets;
    private int maxTicketsCapacity;
    private int soldTicketsCount = 0; // New field to track sold tickets

    private SimulationController simulationController = null;

    public TicketPool(int totalTickets, int maxTicketsCapacity) {
        this.totalTickets = totalTickets;
        this.maxTicketsCapacity = maxTicketsCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public TicketPool(int totalTickets, int maxTicketsCapacity, SimulationController simulationController) {
        this(totalTickets, maxTicketsCapacity);
        this.simulationController = simulationController;
    }

    public void addTicket(User user) throws InterruptedException {
        lock.lock();
        try {
            if (!user.isRunning()) return;
            if (tickets.size() == maxTicketsCapacity) {
                String message = "Ticket pool is full. Cannot add more tickets. Waiting for customers to buy tickets.";
                System.out.println(message);
                this.sendTicketPoolUpdate(new TicketEvent(
                        TicketEventType.POOL_FULL,
                        message
                ));
                condition.await();
            } else {
                Ticket ticket = new Ticket(100);
                tickets.add(ticket);
                String message = "Ticket added to the pool." + ticket;
                System.out.println(message);
                this.sendTicketPoolUpdate(new TicketEvent(
                        TicketEventType.TICKET_ADDED,
                        message,
                        null,
                        user.getName(),
                        ticket
                ));
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void removeTicket(User user) throws InterruptedException {
        lock.lock();
        if (!user.isRunning()) return;
        try {
            if (tickets.isEmpty()) {
                String message = "Ticket pool is empty. Cannot remove tickets. Waiting for vendors to add tickets.";
                System.out.println(message);
                this.sendTicketPoolUpdate(new TicketEvent(
                        TicketEventType.POOL_EMPTY,
                        message
                ));
                condition.await();
            } else {
                Ticket ticket = tickets.remove(0).buyTicket();
                soldTicketsCount++; // Increment the sold tickets count
                String message = "Ticket removed from the pool." + ticket;
                System.out.println(message);
                this.sendTicketPoolUpdate(new TicketEvent(
                        TicketEventType.TICKET_PURCHASED,
                        message,
                        user.getName(),
                        null,
                        ticket
                ));
                condition.signalAll();

                if (soldTicketsCount >= totalTickets) {
                    String simulationOverMessage = "All tickets have been sold. Simulation is over.";
                    System.out.println(simulationOverMessage);
                    this.sendTicketPoolUpdate(new TicketEvent(
                            TicketEventType.SIMULATION_OVER,
                            simulationOverMessage
                    ));
                    simulationController.stopSimulation(); // Notify the controller to stop the simulation
                }
            }
        } finally {
            lock.unlock();
        }
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

    private void sendTicketPoolUpdate(TicketEvent ticketEvent) {
        if (simulationController != null) {
            simulationController.sendSimulationUpdate(ticketEvent);
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
            this.maxTicketsCapacity = totalTickets;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
