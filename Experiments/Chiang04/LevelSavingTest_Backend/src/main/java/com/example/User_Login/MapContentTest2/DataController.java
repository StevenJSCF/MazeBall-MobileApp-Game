package com.example.User_Login.MapContentTest2;

import com.example.User_Login.Map.Map;
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
//                                           @RequestBody WorldData worldData,
                                           @RequestBody List<CircleBody> circleBodies) {
        // Save the data
        savedBody = body;
//        savedWorldData = worldData;
        savedCircleBodies = circleBodies;

        return new ResponseEntity<>("Data saved successfully.", HttpStatus.OK);
    }

    @PostMapping("/save1")
    public ResponseEntity<String> saveData(@RequestBody BodyData requestData) {
        // Extract data from requestData
        Body body = requestData.getBody();
//        WorldData worldData = requestData.getWorldData();
        List<CircleBody> circleBodies = requestData.getListOfCircleBodies();

        // Save the data
        savedBody = body;
//        savedWorldData = worldData;
        savedCircleBodies = circleBodies;

        return new ResponseEntity<>("Data saved successfully.", HttpStatus.OK);
    }

//    @PostMapping("/save1")
//    public ResponseEntity<String> saveData(@RequestBody BodyData requestData) {
//        try {
//            List<BodyDto> bodies = requestData.getBodies();
//            List<CircleBodyDto> circleBodies = requestData.getCircleBodies();
//
//            // Process the received BodyDto and CircleBodyDto objects
//            // ...
//
//            // Return a success response
//            return new ResponseEntity<>("Data saved successfully.", HttpStatus.OK);
//        } catch (Exception e) {
//            // Handle any exceptions that may occur during processing
//            return new ResponseEntity<>("Error processing data.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/save1")
//    public ResponseEntity<String> saveData(@RequestBody BodyData requestData) {
//        try {
//            List<Body> bodies = requestData.getBodies();
//            List<CircleBody> circleBodies = requestData.getCircleBodies();
//
//            // Process the received Body and CircleBody objects
//            // ...
//
//            // Return a success response
//            return new ResponseEntity<>("Data saved successfully.", HttpStatus.OK);
//        } catch (Exception e) {
//            // Handle any exceptions that may occur during processing
//            return new ResponseEntity<>("Error processing data.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }






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
