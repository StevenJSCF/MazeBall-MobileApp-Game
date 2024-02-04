package com.example.User_Login.MapContentTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {

    @Autowired
    private SimulationDataRepository simulationDataRepository;

    @PostMapping("/store")
    public ResponseEntity<String> storeSimulationData(@RequestBody SimulationData simulationData) {
        // Save the received data to the database
        simulationDataRepository.save(simulationData);

        // Respond with a success message
        return ResponseEntity.ok("Data stored successfully");
    }

    @PostMapping("/store1")
    public ResponseEntity<String> storeData(@RequestBody SimulationData requestBody) {
        SimulationData a = new SimulationData();
        a.setBodies(requestBody.getBodies());
        a.setWorld(requestBody.getWorld());
        simulationDataRepository.save(requestBody);

        return ResponseEntity.ok("store1 successful.");
    }

}