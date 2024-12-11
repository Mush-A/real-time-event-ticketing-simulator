package com.realtimeeventticketing.core.simulation;

import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.Ticket;
import com.realtimeeventticketing.core.tickets.TicketPool;
import com.realtimeeventticketing.core.users.Customer;
import com.realtimeeventticketing.core.users.Vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages the simulation of ticket sales, including vendors and customers.
 */
public class Simulation {
    private final List<Vendor> vendors;
    private final List<Customer> customers;
    private final TicketPool ticketPool;
    private final ExecutorService executorService;
    private final Lock vendorLock = new ReentrantLock();
    private final Lock customerLock = new ReentrantLock();

    private final SimulationConfig config;

    /**
     * Constructs a Simulation with the specified configuration and observer.
     *
     * @param config the simulation configuration
     * @param observer the observer for ticket pool events
     */
    public Simulation(SimulationConfig config, ITicketPoolObserver observer) {
        this.config = config;

        this.ticketPool = new TicketPool(config);
        this.ticketPool.addObserver(observer);

        this.vendors = buildVendors(config.getNumVendors(), config.getTicketReleaseRate());
        this.customers = buildCustomers(config.getNumCustomers(), config.getCustomerRetrievalRate());

        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Builds a list of vendors.
     *
     * @param numVendors the number of vendors
     * @param ticketReleaseRate the ticket release rate
     * @return the list of vendors
     */
    private List<Vendor> buildVendors(int numVendors, int ticketReleaseRate) {
        List<Vendor> v = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            v.add(new Vendor(ticketPool, ticketReleaseRate));
        }
        return v;
    }

    /**
     * Builds a list of customers.
     *
     * @param numCustomers the number of customers
     * @param customerRetrievalRate the customer retrieval rate
     * @return the list of customers
     */
    private List<Customer> buildCustomers(int numCustomers, int customerRetrievalRate) {
        List<Customer> c = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            c.add(new Customer(ticketPool, customerRetrievalRate));
        }
        return c;
    }

    /**
     * Starts the simulation by submitting vendors and customers to the executor service.
     */
    public void run() {
        vendorLock.lock();
        try {
            for (Vendor vendor : vendors) {
                executorService.submit(vendor);
            }
        } finally {
            vendorLock.unlock();
        }

        customerLock.lock();
        try {
            for (Customer customer : customers) {
                executorService.submit(customer);
            }
        } finally {
            customerLock.unlock();
        }
    }

    /**
     * Stops the simulation and shuts down the executor service.
     *
     * @throws InterruptedException if interrupted while stopping
     */
    public void stop() throws InterruptedException {
        vendorLock.lock();
        try {
            for (Vendor vendor : vendors) {
                vendor.stop();
            }
        } finally {
            vendorLock.unlock();
        }

        customerLock.lock();
        try {
            for (Customer customer : customers) {
                customer.stop();
            }
        } finally {
            customerLock.unlock();
        }

        this.ticketPool.stopAllWaiting();
        Vendor.resetId();
        Customer.resetId();
        Ticket.resetId();

        if (executorService != null) {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
    }

    /**
     * Checks if the simulation is running.
     *
     * @return true if the simulation is running, false otherwise
     */
    public boolean isRunning() {
        return !executorService.isShutdown();
    }

    /**
     * Gets the list of vendors.
     *
     * @return the list of vendors
     */
    public List<Vendor> getVendors() {
        return new ArrayList<>(this.vendors);
    }

    /**
     * Gets the list of customers.
     *
     * @return the list of customers
     */
    public List<Customer> getCustomers() {
        return new ArrayList<>(this.customers);
    }

    /**
     * Gets the ticket pool.
     *
     * @return the ticket pool
     */
    public TicketPool getTicketPool() {
        return this.ticketPool;
    }

    /**
     * Updates the ticket release rate for all vendors.
     *
     * @param newRate the new ticket release rate
     */
    public void updateVendorReleaseRate(int newRate) {
        vendorLock.lock();
        try {
            for (Vendor vendor : vendors) {
                vendor.setRate(newRate);
            }
        } finally {
            vendorLock.unlock();
        }
    }

    /**
     * Updates the customer retrieval rate for all customers.
     *
     * @param newRate the new customer retrieval rate
     */
    public void updateCustomerRetrievalRate(int newRate) {
        customerLock.lock();
        try {
            for (Customer customer : customers) {
                customer.setRate(newRate);
            }
        } finally {
            customerLock.unlock();
        }
    }

    /**
     * Updates the number of vendors.
     *
     * @param newVendorCount the new number of vendors
     */
    public void updateVendors(int newVendorCount) {
        int currentVendorCount = vendors.size();
        if (newVendorCount > currentVendorCount) {
            int numVendorsToAdd = newVendorCount - currentVendorCount;
            List<Vendor> newVendors = buildVendors(numVendorsToAdd, this.config.getTicketReleaseRate());
            vendors.addAll(newVendors);
            newVendors.forEach(executorService::submit);
        } else if (newVendorCount < currentVendorCount) {
            int numVendorsToRemove = currentVendorCount - newVendorCount;
            for (int i = 0; i < numVendorsToRemove; i++) {
                Vendor vendor = vendors.remove(0);
                vendor.stop();
            }
        }
    }

    /**
     * Updates the number of customers.
     *
     * @param newCustomerCount the new number of customers
     */
    public void updateCustomers(int newCustomerCount) {
        int currentCustomerCount = customers.size();
        if (newCustomerCount > currentCustomerCount) {
            int numCustomersToAdd = newCustomerCount - currentCustomerCount;
            List<Customer> newCustomers = buildCustomers(numCustomersToAdd, this.config.getCustomerRetrievalRate());
            customers.addAll(newCustomers);
            newCustomers.forEach(executorService::submit);
        } else if (newCustomerCount < currentCustomerCount) {
            int numCustomersToRemove = currentCustomerCount - newCustomerCount;
            for (int i = 0; i < numCustomersToRemove; i++) {
                Customer customer = customers.remove(0);
                customer.stop();
            }
        }
    }

    /**
     * Updates the ticket pool configuration.
     *
     * @param totalTickets the total number of tickets
     * @param maxTicketsCapacity the maximum tickets capacity
     */
    public void updateTicketPool(int totalTickets, int maxTicketsCapacity) {
        this.ticketPool.setTotalTickets(totalTickets);
        this.ticketPool.setMaxTicketsCapacity(maxTicketsCapacity);
    }

    /**
     * Gets the simulation configuration.
     *
     * @return the simulation configuration
     */
    public SimulationConfig getConfig() {
        return this.config;
    }
}