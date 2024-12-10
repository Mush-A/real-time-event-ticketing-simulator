package com.realtimeeventticketing.web.controllers;

import com.realtimeeventticketing.core.simulation.SimulationRequest;
import com.realtimeeventticketing.web.services.SimulationService;
import com.realtimeeventticketing.core.simulation.SimulationStatusType;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start")
    public String startSimulation(@RequestBody SimulationRequest request) throws InterruptedException {
        return simulationService.startSimulation(request);
    }

    @PostMapping("/stop")
    public String stopSimulation() throws InterruptedException {
        return simulationService.stopSimulation();
    }

    @GetMapping("/status")
    public SimulationStatusType getSimulationStatus() {
        return simulationService.getSimulationStatus();
    }

    @PutMapping("/update")
    public String updateSimulation(@RequestBody SimulationRequest request) {
        return simulationService.updateSimulation(request);
    }

    @GetMapping("/ticket-events")
    public List<TicketEvent> getTicketEvents() {
        return simulationService.getTicketEvents();
    }
}
