package com.example.User_Login.MapContentTest2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DataController {


    private Body savedBody;
    private WorldData savedWorldData;
    private List<CircleBody> savedCircleBodies = new ArrayList<>();

    // Endpoint to save data
    @PostMapping("/save")
    public ResponseEntity<String> saveData(@RequestBody Body body,
                                           @RequestBody WorldData worldData,
                                           @RequestBody List<CircleBody> circleBodies) {
        // Save the data
        savedBody = body;
        savedWorldData = worldData;
        savedCircleBodies = circleBodies;

        return new ResponseEntity<>("Data saved successfully.", HttpStatus.OK);
    }

    @PostMapping("/save1")
    public ResponseEntity<String> saveData(@RequestBody BodyData requestData) {
        // Extract data from requestData
        Body body = requestData.getBody();
        WorldData worldData = requestData.getWorldData();
        List<CircleBody> circleBodies = requestData.getListOfCircleBodies();

        // Save the data
        savedBody = body;
        savedWorldData = worldData;
        savedCircleBodies = circleBodies;

        return new ResponseEntity<>("Data saved successfully.", HttpStatus.OK);
    }




    // Endpoint to get saved data
//    @GetMapping("/get")
//    public ResponseEntity<Map<String, Object>> getData() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("Bodies", savedBody);
//        data.put("world", savedWorldData);
//        data.put("listOfCircleBodies", savedCircleBodies);
//
//        return new ResponseEntity<>(data, HttpStatus.OK);
//    }
}
