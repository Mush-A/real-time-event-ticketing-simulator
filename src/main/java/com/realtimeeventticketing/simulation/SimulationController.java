package com.realtimeeventticketing.simulation;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SimulationController {

    private Simulation simulation;
    private TicketPool ticketPool;

    private final SimpMessagingTemplate messagingTemplate;

    public SimulationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/simulation/start")
    public String startSimulation(@RequestBody SimulationRequest request) throws InterruptedException {

        if (simulation != null && simulation.isRunning()) {
            return "Simulation is already running.";
        }

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

        simulation = new Simulation(vendors, customers);
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

    @GetMapping("/simulation/status")
    public String simulationStatus() {
        if (simulation != null) {
            return SimulationStatusType.RUNNING.name();
        } else {
            return SimulationStatusType.NOT_RUNNING.name();
        }
    }

    public void sendSimulationUpdate(TicketEvent ticketEvent) {
        messagingTemplate.convertAndSend("/topic/simulation", ticketEvent);
    }
}
