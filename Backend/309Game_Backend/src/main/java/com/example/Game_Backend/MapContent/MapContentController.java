package com.example.Game_Backend.MapContent;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.module.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;

@RestController
@Tag(name = "Map Saving API", description = "HTTP mapping for MapContent")
@RequestMapping("api/mapContent")
public class MapContentController {

    @Autowired
    MapContentRepository mapContentRepo;

    @Autowired
    private AccountRepository accountRepository;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//


    @Operation(summary = "Get the user of the currently played map",
            description = "This will get the user and the map based on the id given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "mapContent not found for id: + id")
    })
    @GetMapping("/{userID}/getMapContentByUser")
    public Message getMapContentByUser1(@PathVariable Long userID) {
        MapContent mapContent = mapContentRepo.findByUserId(userID);
        String s = mapContent.getBody();

        if (mapContent == null) {
            Message a = new Message("get user map content failed", "true"); // or any other success messag
            return a;
        }

        Message a = new Message(s, "true"); // or any other success message
        return a;
    }

//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//

    @Operation(summary = "Post the map that the player is currently playing",
            description = "This will check if a user already have a map being played, also checks if its the same map or another map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "error")
    })
    @PostMapping("mapContent/{id}/{mapNumber}/save")
    public Message PostMapContentByBody(@RequestBody MapContent newMap,
                                 @PathVariable @Parameter(description = "Account ID", example = "1") Long id,
                                 @PathVariable @Parameter(description = "Map's number", example = "1") int mapNumber) {
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

    @Operation(summary = "Updates the mapContent (save button)",
            description = "This will save the map that the player is currently playing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @PutMapping("/{userId}/{columnName}/updateColumn")
    public Message updateColumn(
            @PathVariable @Parameter(description = "Account ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "Column Name") String columnName,
            @RequestBody @Parameter(description = "Column Value") MapContent newMap)
    {

        MapContent mapContent = mapContentRepo.findByUserId(userId);

        if (mapContent == null) {
            Message a = new Message("No map content with that user", "false"); // or any other success message
            return a;
        }

        // Use reflection to set the specified column's value
        try {
            Field field = MapContent.class.getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(mapContent, newMap.getBody());
        } catch (Exception e) {
            Message a = new Message(" updateColumn failed", "false"); // or any other success message
                return a;
        }

        mapContentRepo.save(mapContent);

        Message a = new Message("map was saved", "true"); // or any other success message
        return a;

    }

//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//

    @Operation(summary = "Deletes the saved map",
            description = "This will delete the saved map, this will happen when a player finish a game or starts another game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "deleted"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @DeleteMapping("{userId}/delete")
    Message DeleteMapContent(@PathVariable @Parameter(description = "Account ID", example = "1") Long userId) {
        // Fetch the Account by its ID
        MapContent m = mapContentRepo.findByUserId(userId);
        mapContentRepo.deleteById(m.getMap_ID());

        Message a = new Message("deleted", "true"); // or any other success message
        return a;
    }
}
