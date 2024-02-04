package com.example.User_Login.MapContent;

import com.example.User_Login.Accounts.Account;
import com.example.User_Login.Accounts.AccountRepository;

import com.example.User_Login.module.Message;
import com.example.User_Login.module.NumMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Optional;

@RestController
@RequestMapping("api/mapContent")
public class MapContentController {

    @Autowired
    MapContentRepository mapContentRepo;

    @Autowired
    private AccountRepository accountRepository;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

//    @GetMapping("/{userID}/getMapContentByUser")
//    public Message getMapContentByUser1(@PathVariable Long userID) {
//        MapContent mapContent = mapContentRepo.findByUserId(userID);
//
//        if (mapContent == null) {
//            Message error = new Message("mapContent is null", "false");
//            return ResponseEntity.badRequest().body(error).getBody();
//        }
//
//        Message a = new Message(mapContent.getBody(), "true"); // or any other success message
//        return ResponseEntity.ok(a).getBody();
//    }

    @GetMapping("/findByUserId/{userId}")
    public ResponseEntity<MapContent> findByUserId(@PathVariable Long userId) {
        MapContent mapContent = mapContentRepo.findByUserId(userId);

        if (mapContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapContent);
    }

    @GetMapping("/{userID}/getMapContentByUser")
    public ResponseEntity<MapContent> getMapContentByUser(@PathVariable Long userID) {
        MapContent mapContent = mapContentRepo.findByUserId(userID);

        if (mapContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapContent);
    }



//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//

    //When charging the world call this post method so that the table can be stored
//    @PostMapping("mapContent/{id}/save")
//    Message PostMapContentByBody(@RequestBody MapContent newMap, @PathVariable Long id) {
//        // Fetch the Account by its ID
//        Account account = accountRepository.findById(id).orElse(null);
//
//        if (account != null) {
//            // Set the Account for the MapContent
//            newMap.setUser(account);
//
//            // Save the MapContent with the relationship set
//            mapContentRepo.save(newMap);
//
//            Message a = new Message("success", "true"); // or any other success message
//            return ResponseEntity.ok(a).getBody();
//        } else {
//            // Handle the case where the Account with the given ID doesn't exist
//            // You might want to return an error message or handle the situation as needed
//            Message error = new Message("error", "Account with ID " + id + " not found");
//            return ResponseEntity.badRequest().body(error).getBody();        }
//
//    }

    @PostMapping("mapContent/{id}/{mapNumber}/save")
    Message PostMapContentByBody(@RequestBody MapContent newMap, @PathVariable Long id, @PathVariable int mapNumber) {
        // Fetch the Account by its ID
        Account account = accountRepository.findById(id).orElse(null);
        MapContent m = mapContentRepo.findByUserId(id);

        newMap.setMapNumber(mapNumber);

        if (account != null) {

            if(m != null){
                if(m.getMapNumber() == newMap.getMapNumber()) {
                    //hey you are trying to load the same map, do you want to conitues or start over
                    Message a = new Message("this map is being played", "true"); // or any other success message
                    return ResponseEntity.ok(a).getBody();
                }
                else{
                    //hey you are trying to load a different map
                    Message a = new Message("another map is being played", "true"); // or any other success message
                    return ResponseEntity.ok(a).getBody();
                }
            }

            // Set the Account for the MapContent
            newMap.setUser(account);
            newMap.setMapNumber(mapNumber);

            // Save the MapContent with the relationship set
            mapContentRepo.save(newMap);

            Message a = new Message("success", "true"); // or any other success message
            return ResponseEntity.ok(a).getBody();
        } else {
            // Handle the case where the Account with the given ID doesn't exist
            // You might want to return an error message or handle the situation as needed
            Message error = new Message("error", "Account with ID " + id + " not found");
            return ResponseEntity.badRequest().body(error).getBody();        }

    }



//---------------------------------------//
//------------- PUt METHODS -------------//
//---------------------------------------//

    @PutMapping("/{userId}/{columnName}/updateColumn")
    public Message updateColumn(
            @PathVariable Long userId,
            @PathVariable String columnName,
            @RequestBody MapContent newMap)
    {

        MapContent mapContent = mapContentRepo.findByUserId(userId);

        if (mapContent == null) {
            Message a = new Message("No map content with that user", "false"); // or any other success message
            return ResponseEntity.badRequest().body(a).getBody();
        }

        // Use reflection to set the specified column's value
        try {
            Field field = MapContent.class.getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(mapContent, newMap.getBody());
        } catch (Exception e) {
            Message a = new Message("failed", "false"); // or any other success message
                return ResponseEntity.badRequest().body(a).getBody();
        }


        mapContentRepo.save(mapContent);

        Message a = new Message("success", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();

    }

//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//

    //This method will delete the  map content after exiting
    @DeleteMapping("{userId}/delete")
    Message DeleteMapContent(@PathVariable Long userId) {
        // Fetch the Account by its ID
        MapContent m = mapContentRepo.findByUserId(userId);
        mapContentRepo.deleteById(m.getMap_ID());

        Message a = new Message("deleted", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
    }
}
