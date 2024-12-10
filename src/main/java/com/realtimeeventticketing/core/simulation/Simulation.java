package com.realtimeeventticketing.core.simulation;

import com.realtimeeventticketing.core.users.Customer;
import com.realtimeeventticketing.core.users.Vendor;

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
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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

    public void removeCustomers(int customersToRemove) {
        customerLock.lock();
        try {
            int currentSize = this.customers.size();
            int removeCount = Math.min(customersToRemove, currentSize);
            for (int i = 0; i < removeCount; i++) {
                Customer customer = this.customers.remove(0);
                customer.stop();
            }
        } finally {
            customerLock.unlock();
        }
    }

    public void removeVendors(int vendorsToRemove) {
        vendorLock.lock();
        try {
            int currentSize = this.vendors.size();
            int removeCount = Math.min(vendorsToRemove, currentSize);
            for (int i = 0; i < removeCount; i++) {
                Vendor vendor = this.vendors.remove(0);
                vendor.stop();
            }
        } finally {
            vendorLock.unlock();
        }
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(this.customers);
    }

    public List<Vendor> getVendors() {
        return new ArrayList<>(this.vendors);
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
