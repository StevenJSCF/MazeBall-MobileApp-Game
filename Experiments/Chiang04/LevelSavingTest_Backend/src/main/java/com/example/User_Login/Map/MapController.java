package com.example.User_Login.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/map")
public class MapController {

    @Autowired
    MapRepository mapsRepository;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    //This gets the list of all the maps registered
    @GetMapping("maps/all")
    List<Map> GetAllMaps(){
        return mapsRepository.findAll();
    }

    //This gets the map by ID
    @GetMapping("getMapsById/{id}")
    Map fetchDetailsById(@PathVariable Long id){
        return mapsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map not found for id: " + id));
    }

//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//

    //This sets the attributes of the map by using the url
    @PostMapping("map/post/{map_score}/{creator}/{player_HighScore}/{HighScore_Holder}/{currency_granted}/{xp_granted}")
    Map PostMapByPath(@PathVariable int map_score, @PathVariable String creator, @PathVariable int player_HighScore,
                      @PathVariable String HighScore_Holder, @PathVariable int currency_granted,
                      @PathVariable int xp_granted){
        Map newMap = new Map();
        newMap.setMap_score(map_score);
        newMap.setCreator(creator);
        newMap.setPlayer_HighScore(player_HighScore);
        newMap.setHighScore_Holder(HighScore_Holder);
        newMap.setCurrency_granted(currency_granted);
        newMap.setXp_granted(xp_granted);
        mapsRepository.save(newMap);
        return newMap;
    }

    @PostMapping("newMap/post")
    Map PostMapByBody(Map newMap){
        mapsRepository.save(newMap);
        return newMap;
    }

//---------------------------------------//
//------------- UPDATE METHODS -------------//
//---------------------------------------//


//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//



}
