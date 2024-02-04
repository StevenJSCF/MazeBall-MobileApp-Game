package com.example.Game_Backend.Accounts;


import com.example.Game_Backend.Player.Player;
import com.example.Game_Backend.module.Message;
import com.example.Game_Backend.Player.PlayerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Account API", description = "HTTP mappings for Accounts")
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PlayerRepository playerRepo;


//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    @Operation(summary = "Get the account by the id", description = "Returns info of the account with the id given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Account not found for id: + id")
    })
    @GetMapping("getAccountById/{id}")
    public Account fetchDetailsById(@PathVariable @Parameter(description = "Account ID", example = "1") Long id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
    }

    @Operation(summary = "Get the username of the account by the id", description = "Returns username of the account with the id given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Username not found for id: + id")
    })
    @GetMapping("/getUsernameById/{id}")
    public String getUsernameById(@PathVariable @Parameter(description = "Account ID", example = "1") Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Username not found for id: " + id));

        String a = "{\"message\":" + "\"" + account.getUsername() + "\""+ "," + "\"status\":\"true\"}";
        return a;
    }

    @Operation(summary = "Returns a List of usernames containing {name}", description = "Returns an ArrayList of Strings of all usernames containing {name}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "no usernames found for {name}")
    })
    @GetMapping("account/namecontains/{name}")
    public List<String> nameContains(@PathVariable @Parameter(description = "the substring used as a search term", example = "Paddy") String name){
        List<String> names=new ArrayList<>();
        List<Account> all=accountRepository.findAll();
        for(Account a:all){
            if(a.getUsername().toLowerCase().contains(name.toLowerCase())){
                names.add(a.getUsername());
            }
        }
        return names;
    }

    @Operation(summary = "Get the account by the username", description = "Returns info of the account with the username given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Account not found for username: + username")
    })
    @GetMapping("account/getAccountByUsername/{username}")
    public Account getAccountByUsername(@PathVariable @Parameter(description = "username of an account", example = "Paddy") String username){
        return accountRepository.findByUsername(username);
    }

//---------------------------------------//
//------------- POST METHODS ------------//
//---------------------------------------//

    @Operation(summary = "Post/Creates an account with a requestbody",
            description = "This will create an account given values in the body. This also checks if the username is unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "username is already in use, please try another one")
    })
    @PostMapping("account/post1")
    public Message createAccount1(@RequestBody Account newAccount) {
        if (accountRepository.findByUsername(newAccount.getUsername()) != null) {
            Message message = new Message("Username is already in use, please try another one", "true");
            return message;
        }

        //Create a for loop so that it iterates until finding an empty id

        Long numberOfAccount = accountRepository.count();

        Long userId = numberOfAccount + 1;

        // Create a new Player and set its money
        Player player = new Player();
        player.setId(userId);
        newAccount.setId(userId);
        player.setMoney(0);

        // Save the player to generate the ID
        playerRepo.save(player);

        newAccount.setPlayer(player);

        accountRepository.save(newAccount);

        Message message = new Message("Success", "true");
        return message;
    }

    @Operation(summary = "User login authorization", description = "This will check if the username and password matches from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "message : userID"),
            @ApiResponse(responseCode = "404", description = "message : -1")
    })
    @PostMapping("/login1")
    public String login1(@RequestBody Account loginRequest) {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Account user = accountRepository.findByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            // User authentication successful
            String a = "{\"message\":" + "\"" + user.getId().toString() + "\""+ "," + "\"status\":\"true\"}";
            return a;
        } else {
            String b = "{\"message\":\"-1\"," + "\"status\":\"false\"}";
            return b;
        }
    }

//---------------------------------------//
//------------- PUT METHODS ------------//
//---------------------------------------//

    @Operation(summary = "Updates the account username", description = "This will update the username of the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Account not found for id: + id")
    })
    @PutMapping("/updateUsernameById/{id}")
    public Account updateUsername(@RequestBody Account updatedAccount,
                           @PathVariable @Parameter(description = "Account ID", example = "1") Long id) {
        Account oldUser = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
        oldUser.setUsername(updatedAccount.getUsername());
        accountRepository.save(oldUser);
        return oldUser;
    }

    @Operation(summary = "Updates the account password", description = "This will update the password of the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Account not found for id: + id")
    })
    @PutMapping("/updatePasswordById/{id}")
    public Account updatePassword(@RequestBody Account updatedPassword,
                           @Parameter(description = "Account ID", example = "1") @PathVariable Long id) {
        Account oldPassword = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
        oldPassword.setPassword(updatedPassword.getPassword());
        accountRepository.save(oldPassword);
        return oldPassword;
    }

//---------------------------------------//
//------------- DELETE METHODS ------------//
//---------------------------------------//

    @Operation(summary = "Deletes an account", description = "This will delete an account given its respective id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Account not found for id: + id")
    })
    @DeleteMapping("/deleteAccountById/{id}")
    String deleteAccount(@PathVariable @Parameter(description = "Account ID", example = "1") Long id) {
        accountRepository.deleteById(id);
        String b = "true";
        return b;
    }

}
