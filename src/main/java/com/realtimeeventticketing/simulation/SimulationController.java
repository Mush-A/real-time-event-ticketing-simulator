package com.realtimeeventticketing.simulation;

import com.realtimeeventticketing.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.tickets.TicketEvent;
import com.realtimeeventticketing.tickets.TicketEventType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SimulationController implements ITicketPoolObserver {

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
    public SimulationStatusType simulationStatus() {
        if (simulationBuilder != null && simulationBuilder.isSimulationRunning()) {
            return SimulationStatusType.RUNNING;
        } else {
            return SimulationStatusType.NOT_RUNNING;
        }
    }

    @PutMapping("/simulation/update")
    public String updateSimulation(@RequestBody SimulationRequest request) {
        if (simulationBuilder != null) {
            simulationBuilder.updateSimulation(request);
            return "Simulation updated.";
        } else {
            return "No simulation is running.";
        }
    }

    @GetMapping("/simulation/ticket-events")
    public List<TicketEvent> getTicketEvents() {
        if (simulationBuilder != null) {
            return simulationBuilder.getTicketEvents();
        }
        return new ArrayList<>();
    }

    @Override
    public void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException {
        sendSimulationUpdate(ticketEvent);

        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            stopSimulation();
        }

        System.out.println(ticketEvent.getMessage());
    }

    public void sendSimulationUpdate(TicketEvent ticketEvent) {
        messagingTemplate.convertAndSend("/topic/simulation", ticketEvent);
    }

}
