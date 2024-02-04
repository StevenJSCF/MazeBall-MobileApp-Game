package com.example.User_Login.Accounts;


import com.example.User_Login.module.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

//---------------------------------------//
//------------- GET METHODS -------------//
//---------------------------------------//

    //This gets the list of all the accounts registered
    @GetMapping("account/all")
    List<Account> GetAllAccounts(){
        return accountRepository.findAll();
    }

    //This get the information of the user by ID from the MySQL tables
    @GetMapping("getAccountById/{id}")
    Account fetchDetailsById(@PathVariable Long id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
    }

    //This gets the username of the account by the ID
    @GetMapping("/getUsernameById/{id}")
    public String getUsernameById(@PathVariable Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found for id: " + id));

        String a = "{\"message\":" + "\"" + account.getUsername() + "\""+ "," + "\"status\":\"true\"}";
        return a;
    }

    //This finds the all the information of the account by the ID
    @GetMapping("/account/{id}")
    Account getUser(@PathVariable Long id) {
        return accountRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

//---------------------------------------//
//------------- POST METHODS ------------//
//---------------------------------------//

    //This post the account using the url on postman
    @PostMapping("account/post/{u}/{p}/{ut}")
    Account PostAccountByPath(@PathVariable String u, @PathVariable String p, @PathVariable String ut){
        Account newAccount = new Account();
        newAccount.setUsername(u);
        newAccount.setPassword(p);
        newAccount.setUser_type(ut);
        accountRepository.save(newAccount);
        return newAccount;
    }

    //This post an account in MySQL table with JSON on postman
    @PostMapping("account/post")
    Account CreateAccount(@RequestBody Account newAccount){
        accountRepository.save(newAccount);
        return newAccount;
    }

    //This post an account in MySQL table with JSON on postman and return a success message to the frontend
    @PostMapping("account/post1")
    Message CreateAccount1(@RequestBody Account newAccount){
        accountRepository.save(newAccount);
        Message a = new Message("success", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
    }

    //Temporary test storing the string of the mapObjects just a string of 1s
    @PostMapping("account/post2")
    String StoringMapObjects(@RequestBody Account newAccount){
        accountRepository.save(newAccount);
        String a = "{\"message\":" + "\"" + newAccount.getId().toString() + "\""+ "," + "\"status\":\"true\"}";
        return a;
    }


    //User authentication this checks whether the username and password mathc in MySQL
    @PostMapping("/login")
    public Message login(@RequestBody Account loginRequest) {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Account user = accountRepository.findByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            Message a = new Message("ID", user.getId().toString()); // or any other success message
            return ResponseEntity.ok(a).getBody();
        } else {
            Message b = new Message("failed", "false"); // or any other failure message
            return ResponseEntity.badRequest().body(b).getBody();
        }
    }

    //This is the same as login but the return message is done manually
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

    // Updating the account user by the ID
    @PutMapping("/updateUsernameById/{id}")
    Account updateUsername(@RequestBody Account updatedAccount, @PathVariable Long id) {
        Account oldUser = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
        oldUser.setUsername(updatedAccount.getUsername());
        accountRepository.save(oldUser);
        return oldUser;
    }

    // Updating the account password by the ID
    @PutMapping("/updatePasswordById/{id}")
    Account updatePassword(@RequestBody Account updatedPassword, @PathVariable Long id) {
        Account oldPassword = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
        oldPassword.setPassword(updatedPassword.getPassword());
        accountRepository.save(oldPassword);
        return oldPassword;
    }

//---------------------------------------//
//------------- DELETE METHODS ------------//
//---------------------------------------//

    @DeleteMapping("/deleteAccountById/{id}")
    String deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        String b = "true";
        return b;
    }

}
