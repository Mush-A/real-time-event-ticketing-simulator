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
    private TicketPool ticketPool;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/simulation/start")
    public String startSimulation(@RequestBody SimulationRequest request) throws InterruptedException {

        // Build the configuration using the builder pattern
        ConfigurationBuilder configBuilder = new ConfigurationBuilder(null)
                .setTotalTickets(request.getTotalTickets())
                .setTicketReleaseRate(request.getTicketReleaseRate())
                .setCustomerRetrievalRate(request.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(request.getMaxTicketsCapacity())
                .setNumVendors(request.getNumVendors())
                .setNumCustomers(request.getNumCustomers());

        // Create TicketPool, Vendors, and Customers
        ticketPool = configBuilder.buildTicketPool(this);
        List<Vendor> vendors = configBuilder.buildVendors(ticketPool);
        List<Customer> customers = configBuilder.buildCustomers(ticketPool);

        // Initialize the simulation
        simulation = new Simulation(vendors, customers);

        // Run the simulation asynchronously
        simulation.run();

        return "Simulation started";
    }


    @PostMapping("/simulation/stop")
    public String stopSimulation() throws InterruptedException {
        if (simulation != null) {
            simulation.stop();
            ticketPool.stopAllWaiting();
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
