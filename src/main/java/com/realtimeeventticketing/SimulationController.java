package com.realtimeeventticketing;

import com.realtimeeventticketing.entities.Customer;
import com.realtimeeventticketing.entities.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SimulationController {

    private Simulation simulation;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/simulation/start")
    public String startSimulation(@RequestBody SimulationRequest request) {
        // Build the configuration using the builder pattern
        ConfigurationBuilder configBuilder = new ConfigurationBuilder(null)
                .setTotalTickets(request.getTotalTickets())
                .setTicketReleaseRate(request.getTicketReleaseRate())
                .setCustomerRetrievalRate(request.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(request.getMaxTicketsCapacity())
                .setNumVendors(request.getNumVendors())
                .setNumCustomers(request.getNumCustomers());

        // Create TicketPool, Vendors, and Customers
        TicketPool ticketPool = configBuilder.buildTicketPool(this);
        List<Vendor> vendors = configBuilder.buildVendors(ticketPool);
        List<Customer> customers = configBuilder.buildCustomers(ticketPool);

        // Initialize the simulation
        simulation = new Simulation(vendors, customers);

        // Run the simulation asynchronously
        new Thread(() -> {
            try {
                simulation.run(request.getDurationInSeconds());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return "Simulation started for " + request.getDurationInSeconds() + " seconds.";
    }


    @PostMapping("/simulation/stop")
    public String stopSimulation() throws InterruptedException {
        if (simulation != null) {
            simulation.stop();
            return "Simulation stopped.";
        } else {
            return "No simulation is running.";
        }
    }

    @GetMapping("/")
    public void sendSimulationUpdate(TicketEvent ticketEvent) {
        messagingTemplate.convertAndSend("/topic/simulation", ticketEvent);
    }
}
