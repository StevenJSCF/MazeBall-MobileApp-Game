package com.example.User_Login.MapContent;

import com.example.User_Login.module.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MapContentController {

    @Autowired
    MapContentRepository mapContentRepo;

    @PostMapping("mapContent/save")
    Message PostMapContentByBody(@RequestBody MapContent newMap){
        mapContentRepo.save(newMap);

        Message a = new Message("success", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
    }

//    @GetMapping("getMapContent")
//    MapContent GetMapContent(@PathVariable MapContent map){
//        MapContentRepository a = new mapContentRepo.findById(1)
//
//    }

//    @GetMapping("getAccountById/{id}")
//    MapContent fetchDetailsById(@PathVariable Long id){
//        return mapContentRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
//    }


}
