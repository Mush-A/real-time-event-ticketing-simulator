package com.realtimeeventticketing;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    @PostMapping("/start-simulation")
    public void startSimulation() {
        // Start the simulation
    }
}
