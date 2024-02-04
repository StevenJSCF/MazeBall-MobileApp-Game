package coms309.demo2.test.controller;

import coms309.demo2.test.repository.AccountRepository;
import org.json.JSONException;

//import org.json.JSONObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import coms309.demo2.test.module.Account;


//import org.json.JSONObject;
//import org.springframework.boot.configurationprocessor.json.JSONObject


import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

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

    //This post the account in the url on postman
    @PostMapping("account/post/{u}/{p}/{ut}")
    Account PostAccountByPath(@PathVariable String u, @PathVariable String p, @PathVariable String ut){
        Account newAccount = new Account();
        newAccount.setUsername(u);
        newAccount.setPassword(p);
        newAccount.setUser_type(ut);
        accountRepository.save(newAccount);
        return newAccount;
    }

    //This post an account with JSON on postman
    @PostMapping("account/post")
<<<<<<< HEAD
    Account PostAccountByPath( Account newAccount){
=======
    Account PostAccountByPath(@RequestBody Account newAccount){
>>>>>>> origin/main
        accountRepository.save(newAccount);
        return newAccount;
    }

    @GetMapping("/account/{id}")
    Account getUser(@PathVariable Long id) {
        return accountRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

    // Updating the account user
    @PutMapping("/updateUsernameById/{id}")
    Account updateUsername(@RequestBody Account updatedAccount, @PathVariable Long id) {
        Account oldUser = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
        oldUser.setUsername(updatedAccount.getUsername());
        accountRepository.save(oldUser);
        return oldUser;
    }

    // Updating the account password
    @PutMapping("/updatePasswordById/{id}")
    Account updatePassword(@RequestBody Account updatedPassword, @PathVariable Long id) {
        Account oldPassword = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
        oldPassword.setPassword(updatedPassword.getPassword());
        accountRepository.save(oldPassword);
        return oldPassword;
    }

    @DeleteMapping("/deleteAccountById/{id}")
    String deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return "Deleted Account with id:" + id;
    }


    //WORKING ON THE AUTHENTICATION
<<<<<<< HEAD
//    @GetMapping("/login/{username}/{password}")
//    public boolean login(@PathVariable String username, @PathVariable String password) {
//        // Authenticate the user based on the provided username and password
//        // Check if the username and password match a user account in the database
//
//        Account user = accountRepository.findByUsername(username);
//        return user != null && user.getPassword().equals(password);
//    }

//    @PostMapping("/login")
//    public boolean login(@RequestBody Account loginRequest) {
//        // Authenticate the user based on the provided username and password
//        // Check if the username and password match a user account in the database
//
//
//        Account user = accountRepository.findByUsername(loginRequest.getUsername());
//        return user != null && user.getPassword().equals(loginRequest.getPassword());
//    }


    @PostMapping("/login")
    public ResponseEntity<String> login(Account loginRequest) throws JSONException {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Account user = accountRepository.findByUsername(loginRequest.getUsername());

        JSONObject responseObject = new JSONObject();
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            responseObject.put("message", "Authentication successful");
            responseObject.put("success", true);
            return ResponseEntity.ok(responseObject.toString());
        } else {
            responseObject.put("message", "Authentication failed");
            responseObject.put("success", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseObject.toString());
        }
=======
    @GetMapping("/login/{username}/{password}")
    public boolean login(@PathVariable String username, @PathVariable String password) {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Account user = accountRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
>>>>>>> origin/main
    }
}
