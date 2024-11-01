package com.realtimeeventticketing;

import com.realtimeeventticketing.ConfigurationBuilder;
import com.realtimeeventticketing.entities.Customer;
import com.realtimeeventticketing.entities.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private Simulation simulation;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/start")
    public String startSimulation(
            @RequestParam int totalTickets,
            @RequestParam int ticketReleaseRate,
            @RequestParam int customerRetrievalRate,
            @RequestParam int maxTicketsCapacity,
            @RequestParam int numVendors,
            @RequestParam int numCustomers,
            @RequestParam int durationInSeconds) throws InterruptedException {

        // Build the configuration using the builder pattern
        ConfigurationBuilder configBuilder = new ConfigurationBuilder(null)
                .setTotalTickets(totalTickets)
                .setTicketReleaseRate(ticketReleaseRate)
                .setCustomerRetrievalRate(customerRetrievalRate)
                .setMaxTicketsCapacity(maxTicketsCapacity)
                .setNumVendors(numVendors)
                .setNumCustomers(numCustomers);

        // Create TicketPool, Vendors, and Customers
        TicketPool ticketPool = configBuilder.buildTicketPool(this);
        List<Vendor> vendors = configBuilder.buildVendors(ticketPool);
        List<Customer> customers = configBuilder.buildCustomers(ticketPool);

        // Initialize and start the simulation
        simulation = new Simulation(vendors, customers);
        simulation.run(durationInSeconds);

        return "Simulation started for " + durationInSeconds + " seconds.";
    }

    @PostMapping("/stop")
    public String stopSimulation() throws InterruptedException {
        if (simulation != null) {
            simulation.stop();
            return "Simulation stopped.";
        } else {
            return "No simulation is running.";
        }
    }

    public void sendSimulationUpdate(String update) {
        messagingTemplate.convertAndSend("/topic/simulation", update);
    }
}
