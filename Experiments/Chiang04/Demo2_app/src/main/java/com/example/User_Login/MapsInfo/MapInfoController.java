package com.example.User_Login.MapsInfo;

import com.example.User_Login.MapContent.MapContent;
import com.example.User_Login.PlayerMapInfo.Map;
import com.example.User_Login.module.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mapInfo")
public class MapInfoController {

    @Autowired
    MapInfoRepository mapInfoRepo;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

@GetMapping("getMapHighScore/{mapNumber}")
Message getMapHighScore(@PathVariable Long mapNumber){

    Message a = new Message("success", "true");
    return ResponseEntity.ok(a).getBody();
}

    @GetMapping("getMapHighScore/{id}")
    public ResponseEntity<MapInfo> getMapHighScore1(@PathVariable Long mapNumber) {
    //Here it is find by id because the map number its the same as the map id
        MapInfo mapInfo = mapInfoRepo.findById(mapNumber).orElseThrow(() -> new RuntimeException("mapHighScore not found for id: " + mapNumber));;

        if (mapInfo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapInfo);
    }

//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//

    @PostMapping("map/post/{creator}/{map_highestScore}/{username}")
    Message PostMapByPath(@PathVariable String creator ,@PathVariable int map_highestScore, @PathVariable String username){
        MapInfo newMap = new MapInfo();
        newMap.setCreator(creator);
        newMap.setMap_highestScore(map_highestScore);
        newMap.setHighScoreHolder(username);

        mapInfoRepo.save(newMap);

        Message a = new Message("success", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
    }

//---------------------------------------//
//------------- PUT METHODS -------------//
//---------------------------------------//

    @PutMapping("updateMapHighScore/{newHighScore}/{mapId}/{username}")
    Message updateHighScore(@PathVariable int newHighScore, @PathVariable Long mapId, @PathVariable String username){
    //Here the maop number its the same as the id so it is using long
        MapInfo m = mapInfoRepo.findById(mapId).orElseThrow(() -> new RuntimeException("mapInfo not found for id: " + mapId));

        m.setMap_highestScore(newHighScore);
        m.setHighScoreHolder(username);
        mapInfoRepo.save(m);

        Message a = new Message("highScore updated", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
    }

//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//


}
