package com.example.User_Login.MapContent;

import com.example.User_Login.module.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/mapContent")
public class MapContentController {

    @Autowired
    MapContentRepository mapContentRepo;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    @GetMapping("/{userID}/getMapContentByUser")
    public ResponseEntity<MapContent> getMapContentByUser(@PathVariable Long userID) {
        Optional<MapContent> mapContentOptional = mapContentRepo.findById(userID);

        if (mapContentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MapContent mapContent = mapContentOptional.get();

        return ResponseEntity.ok(mapContent);
    }

    @GetMapping("/{userID}/getMapContentByUser1")
    public ResponseEntity<MapContent> getMapContentByUser1(@PathVariable Long userID) {
        MapContent mapContent = mapContentRepo.findByUserId(userID);

        if (mapContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapContent);
    }




//Get user ID and return the map content
//    @GetMapping("/{userID}/getMapContent")
//    public ResponseEntity<MapContent> getMapContentByUser1(@PathVariable Long userID) {
//       mapContent = mapContentRepo.findByUserID(userID);
//
//        if (mapContentOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        MapContent mapContent = mapContentOptional.get();
//
//        return ResponseEntity.ok(mapContent);
//    }






//    @GetMapping("getAccountById/{id}")
//    MapContent fetchDetailsById(@PathVariable Long id){
//        return mapContentRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
//    }


//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//

    @PostMapping("mapContent/{id}/save")
    Message PostMapContentByBody(@RequestBody MapContent newMap, @PathVariable Long id){
        newMap.setUser_ID(id);
        mapContentRepo.save(newMap);

        Message a = new Message("success", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
    }

//---------------------------------------//
//------------- PUt METHODS -------------//
//---------------------------------------//

    @PutMapping("/{id}/{columnName}/{newValue}/updateColumn")
    public ResponseEntity<?> updateColumn(
            @PathVariable Long id,
            @PathVariable String columnName,
            @PathVariable String newValue) {

        MapContent entityToUpdate = mapContentRepo.findById(id).orElse(null);

        if (entityToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        // Use reflection to set the specified column's value
        try {
            Field field = MapContent.class.getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(entityToUpdate, newValue);
        } catch (Exception e) {
            // Handle exceptions, e.g., column not found
            return ResponseEntity.badRequest().body("Invalid column name: " + columnName);
        }

        mapContentRepo.save(entityToUpdate);

        return ResponseEntity.ok("Column updated successfully");
    }

//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//


}
