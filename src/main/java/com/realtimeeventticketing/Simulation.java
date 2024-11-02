package com.realtimeeventticketing;

import com.realtimeeventticketing.entities.Customer;
import com.realtimeeventticketing.entities.Vendor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulation {
    private final List<Vendor> vendors;
    private final List<Customer> customers;
    private final ExecutorService executorService;

    public Simulation(List<Vendor> vendors, List<Customer> customers) {
        this.vendors = vendors;
        this.customers = customers;
        this.executorService =  Executors.newFixedThreadPool(vendors.size() + customers.size());
    }

    public void run(int durationInSeconds) throws InterruptedException {
        vendors.forEach(executorService::submit);
        customers.forEach(executorService::submit);
    }

    public void stop() throws InterruptedException {
        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        if (executorService != null) {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();  // Force shutdown if tasks are not terminating
            }
        }
    }
}
