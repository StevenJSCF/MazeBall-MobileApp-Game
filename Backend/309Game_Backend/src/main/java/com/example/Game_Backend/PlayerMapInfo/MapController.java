package com.example.Game_Backend.PlayerMapInfo;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.MapContent.MapContent;
import com.example.Game_Backend.MapsInfo.MapInfo;
import com.example.Game_Backend.MapsInfo.MapInfoRepository;
import com.example.Game_Backend.module.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@Tag(name = "Player Maps API", description = "HTTP request mappings for Maps")
@RequestMapping("api/map")
public class MapController {
    @Autowired
    MapRepository mapsRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MapInfoRepository mapInfoRepo;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    //This gets the map by ID
    @Operation(summary = "Gets the player map info for its respective map",
            description = "Given the id this will give back the player map info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Map not found for id: + id")
    })
    @GetMapping("getMapsById/{id}")
    Map fetchDetailsById(@PathVariable @Parameter(description = "Map ID", example = "1") Long id){
        return mapsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map not found for id: " + id));
    }


    @Operation(summary = "Gets the player highScore of the specified map ",
            description = "Based on the map number and id giving this will give back the hishScore since a user can have several maps")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @GetMapping("getPlayerHighScore/{mapNumber}/{userId}")
    public Message getPlayerHighScore(@PathVariable @Parameter(description = "Map ID", example = "1") int mapNumber,
                               @PathVariable @Parameter(description = "Account ID", example = "1") Long userId){

        Map m = mapsRepository.findByAccountIdAndMapNumber(userId, mapNumber).orElse(null);

       int playerHighScore = m.getPlayer_HighScore();

        if(m!=null){
        Message a = new Message("HighScore " + Integer.toString(playerHighScore) + "Score", "true");
        return a;
        }

        else {
            Message a = new Message("failed", "false");
            return a;
        }
    }


    public int calculateTotalHighScoreForPlayer(int playerId) {
        long amountOfMaps = mapInfoRepo.count();
        int am = (int) amountOfMaps;
        int playerSum = 0;

        Account m1 = accountRepository.findById(Long.valueOf(playerId)).orElse(null);

        if(m1 == null){
            return playerSum = -1;
        }


        for (int j = 1; j <= am; j++) {
            Map m = mapsRepository.findByAccountIdAndMapNumber(Long.valueOf(playerId), j).orElse(null);

            if (m != null) {
                playerSum += m.getPlayer_HighScore();
            }
        }

        return playerSum;
    }


    @GetMapping("top10LeaderBoard")
    public Message getLeaderBoard(){
        long amountOfUser = accountRepository.count();
        int au = (int) amountOfUser;

        List<PlayerScore> playerScores = new ArrayList<>();

        for (int i = 1; i <= au; i++) {
            int playerSum = calculateTotalHighScoreForPlayer(i);


            if(playerSum != -1) {
                playerScores.add(new PlayerScore(i, playerSum));
            }
        }

        // Sort the player scores in descending order
        playerScores.sort(Comparator.comparingInt(PlayerScore::getHighScore).reversed());

        // Take the top 10 players
        List<PlayerScore> top10Players = playerScores.subList(0, Math.min(10, playerScores.size()));

        // Build the response message
        StringBuilder messageBuilder = new StringBuilder();
        for (PlayerScore player : top10Players) {
            Account m = accountRepository.findById(Long.valueOf(player.getPlayerId())).orElse(null);

            messageBuilder.append(m.getUsername()).append(",").append(player.getHighScore() + ":");
        }

        return new Message(messageBuilder.toString(), "true");
    }


//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//
    @Operation(summary = "Post/creates another map record when the player start playing that map",
            description = "The way this works is that when the user starts playing a new map, this will create a row in the table to store " +
                    "the values of that certain map that the user is playing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "failed")
    })

    @PostMapping("newMap/post/{mapNumber}/{userId}")
    public Message postMapByBody(
            @PathVariable @Parameter(description = "Map ID", example = "1") int mapNumber,
            @PathVariable @Parameter(description = "Account ID", example = "1") Long userId) {

        Map tempMap = mapsRepository.findByAccountIdAndMapNumber(userId, mapNumber).orElse(null);

        if (tempMap != null) {
            // Map already exists
            Message message = new Message("Map already exists (ignore)", "false");
            return message;
        }

        // Map doesn't exist, create a new one
        Map newMap = new Map();
        Account account = accountRepository.findById(userId).orElse(null);

        if (account == null) {
            // Handle the case where the account is not found
            Message message = new Message("Account not found", "false");
            return message;
        }

        newMap.setMapNumber(mapNumber);
        newMap.setUser(account);
        newMap.setPlayer_HighScore(0);

        mapsRepository.save(newMap);

        Message message = new Message("Map was saved", "true");
        return message;
    }

//---------------------------------------//
//------------- UPDATE METHODS -------------//
//---------------------------------------//

    @Operation(summary = "Updates the player highscore for that map if beaten",
            description = "This will update the highscore of that specific player for that map (if beaten)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "failed")
    })
    @PutMapping("updatePlayerMapHighScore/{newPlayerHighScore}/{mapNumber}/{userId}")
    public Message updatePlayerHighScore(@PathVariable int newPlayerHighScore,
                                  @PathVariable @Parameter(description = "Map ID", example = "1") int mapNumber,
                                  @PathVariable @Parameter(description = "Account ID", example = "1") Long userId){
        //Here the maop number its the same as the id so it is using long
//        Map m = mapsRepository.findById(mapId).orElseThrow(() -> new RuntimeException("mapInfo not found for id: " + mapId));
        Map m1 = mapsRepository.findByAccountIdAndMapNumber(userId, mapNumber).orElse(null);
        //Converting the map number into a long so i can search by Id

        if(m1 == null){
            Message a = new Message("couldnt find a column to update with that mapNumber and UserId", "true"); // or any other success message
            return a;
        }

        if(m1.getPlayer_HighScore() >= newPlayerHighScore){
            Message a = new Message(String.valueOf(m1.getPlayer_HighScore())+ "   Score", "true"); // or any other success message
            return a;
        }

            m1.setPlayer_HighScore(newPlayerHighScore);
            mapsRepository.save(m1);

        Message a = new Message("new highscore", "true"); // or any other success message
        return a;
    }


//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//



}
