package com.realtimeeventticketing.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation {
    private final List<Vendor> vendors;
    private final List<Customer> customers;
    private final ExecutorService executorService;
    private final Lock vendorLock = new ReentrantLock();
    private final Lock customerLock = new ReentrantLock();

    public Simulation(List<Vendor> vendors, List<Customer> customers) {
        this.vendors = new ArrayList<>(vendors);
        this.customers = new ArrayList<>(customers);
        this.executorService = Executors.newFixedThreadPool(vendors.size() + customers.size());
    }

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

        if (executorService != null) {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();  // Force shutdown if tasks are not terminating
            }
        }
    }

    public boolean isRunning() {
        return !executorService.isShutdown();
    }


    public void addCustomers(List<Customer> customers) {
        customerLock.lock();
        try {
            this.customers.addAll(customers);
            if (isRunning()) {
                for (Customer customer : customers) {
                    executorService.submit(customer);
                }
            }
        } finally {
            customerLock.unlock();
        }
    }

    public void addVendors(List<Vendor> vendors) {
        vendorLock.lock();
        try {
            this.vendors.addAll(vendors);
            if (isRunning()) {
                for (Vendor vendor : vendors) {
                    executorService.submit(vendor);
                }
            }
        } finally {
            vendorLock.unlock();
        }
    }

    public void removeCustomers(int customers) {
        customerLock.lock();
        try {
            // Stop and discard the first n customers
            for (int i = 0; i < customers; i++) {
                this.customers.get(i).stop();
            }

            // Remove the first n customers
            this.customers.subList(0, customers).clear();
        } finally {
            customerLock.unlock();
        }
    }

    public void removeVendors(int vendors) {
        vendorLock.lock();
        try {
            // Stop and discard the first n vendors
            for (int i = 0; i < vendors; i++) {
                this.vendors.get(i).stop();
            }

            // Remove the first n vendors
            this.vendors.subList(0, vendors).clear();
        } finally {
            vendorLock.unlock();
        }
    }

    public List<Customer> getCustomers(){
        return this.customers;
    }

    public List<Vendor> getVendors(){
        return this.vendors;
    }

    public void setCustomerRetrievalRate(int rate) {
        customerLock.lock();
        try {
            for (Customer customer : customers) {
                customer.setRate(rate);
            }
        } finally {
            customerLock.unlock();
        }
    }

    public void setVendorReleaseRate(int rate) {
        vendorLock.lock();
        try {
            for (Vendor vendor : vendors) {
                vendor.setRate(rate);
            }
        } finally {
            vendorLock.unlock();
        }
    }
}