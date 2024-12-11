package com.realtimeeventticketing.web.controllers;

import com.realtimeeventticketing.core.simulation.SimulationRequest;
import com.realtimeeventticketing.web.persistence.SimulationConfigEntity;
import com.realtimeeventticketing.web.services.SimulationService;
import com.realtimeeventticketing.core.simulation.SimulationStatusType;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling simulation-related requests.
 */
@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    /**
     * Constructs a SimulationController with the specified SimulationService.
     *
     * @param simulationService the simulation service
     */
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Starts a new simulation with the given request.
     *
     * @param request the simulation request
     * @return a message indicating the result of the operation
     */
    @PostMapping("/start")
    public String startSimulation(@RequestBody SimulationRequest request) {
        return simulationService.startSimulation(request);
    }

    /**
     * Stops the currently running simulation.
     *
     * @return a message indicating the result of the operation
     */
    @PostMapping("/stop")
    public String stopSimulation() {
        return simulationService.stopSimulation();
    }

    /**
     * Gets the current status of the simulation.
     *
     * @return the current simulation status
     */
    @GetMapping("/status")
    public SimulationStatusType getSimulationStatus() {
        return simulationService.getSimulationStatus();
    }

    /**
     * Updates the simulation with the given request.
     *
     * @param request the simulation request
     * @return a message indicating the result of the operation
     */
    @PutMapping("/update")
    public String updateSimulation(@RequestBody SimulationRequest request) {
        return simulationService.updateSimulation(request);
    }

    /**
     * Gets the list of ticket events.
     *
     * @return the list of ticket events
     */
    @GetMapping("/ticket-events")
    public List<TicketEvent> getTicketEvents() {
        return simulationService.getTicketEvents();
    }

    /**
     * Gets all simulation configurations.
     *
     * @return the list of all simulation configurations
     */
    @GetMapping("/get-all-simulation-config")
    public List<SimulationConfigEntity> getAllSimulationConfig() {
        return simulationService.getAllSimulationConfig();
    }
}