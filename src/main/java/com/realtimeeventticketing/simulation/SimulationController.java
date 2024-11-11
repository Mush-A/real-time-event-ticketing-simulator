package com.realtimeeventticketing.simulation;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimulationController {

    private final SimpMessagingTemplate messagingTemplate;
    private SimulationBuilder simulationBuilder;

    public SimulationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/simulation/start")
    public String startSimulation(@RequestBody SimulationRequest request) throws InterruptedException {
        if (simulationBuilder != null && simulationBuilder.isSimulationRunning()) {
            return "Simulation is already running.";
        }

        // Build the simulation using the builder pattern
        simulationBuilder = new SimulationBuilder()
                .setTotalTickets(request.getTotalTickets())
                .setTicketReleaseRate(request.getTicketReleaseRate())
                .setCustomerRetrievalRate(request.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(request.getMaxTicketsCapacity())
                .setNumVendors(request.getNumVendors())
                .setNumCustomers(request.getNumCustomers())
                .buildTicketPool(this)
                .buildSimulation();

        simulationBuilder.startSimulation();
        return "Simulation started";
    }

    @PostMapping("/simulation/stop")
    public String stopSimulation() throws InterruptedException {
        if (simulationBuilder != null) {
            simulationBuilder.stopSimulation();
            return "Simulation stopped.";
        } else {
            return "No simulation is running.";
        }
    }

    @GetMapping("/simulation/status")
    public String simulationStatus() {
        if (simulationBuilder != null && simulationBuilder.isSimulationRunning()) {
            return SimulationStatusType.RUNNING.name();
        } else {
            return SimulationStatusType.NOT_RUNNING.name();
        }
    }

    public void sendSimulationUpdate(TicketEvent ticketEvent) {
        messagingTemplate.convertAndSend("/topic/simulation", ticketEvent);
    }
}
