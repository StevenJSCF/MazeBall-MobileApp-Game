package com.example.Game_Backend.CollaborativeEditing;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.PlayerMapInfo.Map;
import com.example.Game_Backend.module.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Collaborative Editing API", description = "HTTP request mappings for collaborative editing")
@RequestMapping("api/EditingMap")
public class EditingMapController {

    @Autowired
    EditingMapRepository editingMapRepo;

    @Autowired
    AccountRepository accountRepo;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    @Operation(summary = "Get the last change made in the editing session",
            description = "This will get the last change made in the editing session, this is fo the undo button")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    @GetMapping("getLastChange/{sessionID}")
    public Message getLastChange(@PathVariable Long sessionID) {

        EditingMap m = editingMapRepo.findById(sessionID).orElse(null);

        if (m == null){
            Message a = new Message("not existing session num", "fail");
            return a;
        }

        // Find the index of the last space
        int lastIndex = m.getChanges().lastIndexOf(" ");

        // Extract the substring starting from the character after the last space
        String result = m.getChanges().substring(lastIndex + 1);

        Message a = new Message(result, "true");
        return a;
    }
//---------------------------------------//
//------------- POST METHODS -------------//
//---------------------------------------//
    
    //This is for posting the session everytime one is created
    //Add that id the session exists dont create a new session
    @PostMapping("newMap/post/{sessionNum}")
    //it should not be the user ID but like the session id
    public Message postMapByBody(@PathVariable Long sessionNum) {

        EditingMap m = new EditingMap();

        //should be map id
        m.setEditingMap_Id(sessionNum);
        m.setHost(sessionNum);

        m.setChanges("111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111");

        editingMapRepo.save(m);

        Message a = new Message("the session was posted", "true");

        return a;
    }

//---------------------------------------//
//------------- UPDATE METHODS -------------//
//---------------------------------------//



//---------------------------------------//
//------------- DELETE METHODS -------------//
//---------------------------------------//
@Operation(summary = "Delete the last change made in the session",
        description = "This will delete the last change made in the editing session")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "last change was undo"),
        @ApiResponse(responseCode = "404", description = "error")
})
    @DeleteMapping("undoChanges/{sessionID}")
    public Message undoChanges(@PathVariable Long sessionID){
    EditingMapHelper mh = new EditingMapHelper(editingMapRepo);
    EditingMap m = editingMapRepo.findById(sessionID).orElse(null);

    if (m == null){
        Message a = new Message("not existing session num", "fail");
        return a;
    }

    int lastIndex1 = m.getChanges().lastIndexOf(" ");
    // Extract the substring starting from the character after the last space
    String result1 = m.getChanges().substring(lastIndex1 + 1);

    if(result1 == "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"){
        Message a = new Message(null, "false");
        return a;
    }

    else {
        mh.deleteLastChange(sessionID);
        // Find the index of the last space
        int lastIndex = m.getChanges().lastIndexOf(" ");
        // Extract the substring starting from the character after the last space
        String result = m.getChanges().substring(lastIndex + 1);
        Message a = new Message(result, "true");
        return a;
    }

    }


    //Call this when the user exits the editing without finishing
    //or when the user post the map to the game

    @DeleteMapping("/deleteAll/{sessionID}")
    public String deleteAll(@PathVariable Long sessionID) {
        editingMapRepo.deleteById(sessionID);
        return "Session deleted successfully";
    }

}
