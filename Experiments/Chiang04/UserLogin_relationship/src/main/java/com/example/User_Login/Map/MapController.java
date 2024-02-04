package com.example.User_Login.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapController {

    @Autowired
    MapRepository mapsRepository;

    //This gets the list of all the accounts registered
    @GetMapping("maps/all")
    List<Map> GetAllMaps(){
        return mapsRepository.findAll();
    }

    @GetMapping("getMapsById/{id}")
    Map fetchDetailsById(@PathVariable Long id){
        return mapsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map not found for id: " + id));
    }

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

}
