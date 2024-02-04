package com.example.User_Login.PlayerMapInfo;

import com.example.User_Login.Accounts.Account;
import com.example.User_Login.Accounts.AccountRepository;
import com.example.User_Login.MapContent.MapContent;
import com.example.User_Login.module.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/map")
public class MapController {

    @Autowired
    MapRepository mapsRepository;

    @Autowired
    private AccountRepository accountRepository;

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
//    @PostMapping("post/{mapNumber}/{userId}")
//    Message PostMapByPath(@PathVariable int mapNumber, @PathVariable Long userId) {
//
//        Account account = accountRepository.findById(userId).orElse(null);
//        Map m = mapsRepository.findByUserId(userId);
//
//        if (account != null) {
//
//            if(m != null){
//                if(m.getMapNumber() == newMap.getMapNumber()) {
//                    //hey you are trying to load the same map, do you want to conitues or start over
//                    Message a = new Message("this map is being played", "true"); // or any other success message
//                    return ResponseEntity.ok(a).getBody();
//                }
//                else{
//                    //hey you are trying to load a different map
//                    Message a = new Message("another map is being played", "true"); // or any other success message
//                    return ResponseEntity.ok(a).getBody();
//                }
//            }
//
//        Map newMap = new Map();
//        newMap.setMapNumber(mapNumber);
//        newMap.setPlayer_HighScore(0);
//        mapsRepository.save(newMap);
//        return newMap;
//    }

    @PostMapping("newMap/post/{mapNumber}/{userId}")
    public ResponseEntity<Message> postMapByBody(@PathVariable int mapNumber, @PathVariable Long userId) {
        Map newMap = new Map();

        Account account = accountRepository.findById(userId).orElse(null);
        newMap.setMapNumber(mapNumber);
        newMap.setUser(account);

        try {
            mapsRepository.save(newMap);
            Message successMessage = new Message("Success creating the mapInfo", "true");
            return ResponseEntity.ok(successMessage);
        } catch (DataIntegrityViolationException e) {
            // Handle the case of a unique constraint violation
            Message errorMessage = new Message("playerMapinfo with this mapNumber and userId already exists.", "false");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }


//---------------------------------------//
//------------- UPDATE METHODS -------------//
//---------------------------------------//


//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//



}
