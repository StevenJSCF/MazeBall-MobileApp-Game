package com.example.Game_Backend.MapsInfo;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.MapContent.MapContent;
import com.example.Game_Backend.module.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Game Maps API", description = "HTTP request mappings for MapsInfo")
@RequestMapping("api/mapInfo")
public class MapInfoController {

    @Autowired
    MapInfoRepository mapInfoRepo;

    @Autowired
    AccountRepository accountRepo;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    @Operation(summary = "Gets all the mapContent/body of the map",
            description = "This will get the objects that construct the map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    @GetMapping("/{mapNumber}/getMapContentById")
    public Message getMapContentByUser(@PathVariable @Parameter(description = "Map's number", example = "1") Long mapNumber) {
        MapInfo m = mapInfoRepo.findById(mapNumber).orElse(null);

        if (m == null) {
            Message a = new Message("get user map content failed", "true"); // or any other success messag
            return a;
        }

        String s = m.getBody();


        Message a = new Message(s, "true"); // or any other success messag
        return a;
    }

    //this will send the amount of maps

    @Operation(summary = "Gets the number of the maps in the game",
            description = "This will get the number of maps that where created for the game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "level : ammountOfMaps"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @GetMapping("getAmmountOfLevels")
    public String ammountOfLevels(){
        long ammountOfMaps = mapInfoRepo.count();
        String levelCount = Long.toString(ammountOfMaps);

        String a = "{\"levels\":\"" + levelCount + "\"}";
        return a;
    }

//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//


    @Operation(summary = "Post/Creates game maps by admin (test)",
            description = "This is for posting/creating maps of the game, this can be only done by the admin of the game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @PostMapping("map/post/{creator}/{map_highestScore}/{username}")
    public Message PostMapByPath(@PathVariable @Parameter(description = "username of the Map's creator", example = "Nick") String creator,
                          @PathVariable @Parameter(description = "High Score of the Map", example = "999999") int map_highestScore,
                          @PathVariable @Parameter(description = "username of the High Score holder", example = "Josh") String username){
        MapInfo newMap = new MapInfo();
        newMap.setCreator(creator);
        newMap.setMap_highestScore(map_highestScore);
        newMap.setHighScoreHolder(username);

        mapInfoRepo.save(newMap);

        Message a = new Message("success", "true"); // or any other success message
        return a;
    }

    //This is for saving the maps after the editing is done
    //here I just need the creator and the content of the map

    @Operation(summary = "Post/Creates game maps by user in the collaborative editing",
            description = "This is for posting/creating maps of the game, this will be done " +
                    "in the collaborative editing when the user is done editing the map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "map was stored"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @PostMapping("postMap/{userId}")
    public Message postMap(@PathVariable @Parameter(description = "Account ID", example = "1") Long userId,
                    @RequestBody MapInfo map){

        MapInfo newMap = new MapInfo();

        //Here for the creator i'm getting the map user id instead of the creator
        Account account = accountRepo.findById(userId).orElse(null);

        if(account == null){
            Message a = new Message("hey couldnt find account with that id", "true"); // or any other success message
            return a;
        }

        if(map.getBody() == null){
            Message a = new Message("hey the mapContent is null", "true"); // or any other success message
            return a;
        }

        newMap.setCreator(account.getUsername());
        newMap.setBody(map.getBody());

        mapInfoRepo.save(newMap);

        Message a = new Message("player map was stored", "true"); // or any other success message
        return a;
    }

//---------------------------------------//
//------------- PUT METHODS -------------//
//---------------------------------------//


    @Operation(summary = "Updates the mapHighScore if beaten",
            description = "When a player finish playing a game this will check if the user beat the highScore for this certain map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @PutMapping("updateMapHighScore/{newHighScore}/{mapNumber}/{userId}")
    Message updateHighScore(@PathVariable @Parameter(description = "New High Score", example = "9999999999999") int newHighScore,
                            @PathVariable @Parameter(description = "Map's number", example = "1") Long mapNumber,
                            @PathVariable @Parameter(description = "Account ID", example = "1") Long userId){
    //Here the map number is the same as the id so it is using long
        MapInfo m = mapInfoRepo.findById(mapNumber).orElseThrow(() -> new RuntimeException("mapInfo not found for id: " + mapNumber));
        Account b = accountRepo.findById(userId).orElse(null);


        if(m == null) {
            Message a = new Message("couldnt find a map with that id", "true"); // or any other success message
            return a;
        }

        if(newHighScore < m.getMap_highestScore()){
            Message a = new Message("sorry the user didn't beat the mapHighScore", "true"); // or any other success message
            return a;
        }

        m.setMap_highestScore(newHighScore);
        m.setHighScoreHolder(b.getUsername());
        mapInfoRepo.save(m);

        Message a = new Message("map high score was updated", "true"); // or any other success message
        return a;
    }

//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//


}
