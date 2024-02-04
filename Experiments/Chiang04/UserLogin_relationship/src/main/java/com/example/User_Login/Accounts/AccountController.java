package com.example.User_Login.Accounts;


import com.example.User_Login.module.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    Account PostAccountByBody(@RequestBody Account newAccount){
        accountRepository.save(newAccount);
        return newAccount;
    }

    @PostMapping("account/post1")
    Message CreateAccount(@RequestBody Account newAccount){
        accountRepository.save(newAccount);
        Message a = new Message("success", "true"); // or any other success message
        return ResponseEntity.ok(a).getBody();
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

    //User authentication
    @PostMapping("/login")
    public Message login(@RequestBody Account loginRequest) {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Account user = accountRepository.findByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            Message a = new Message("success", "true"); // or any other success message
            return ResponseEntity.ok(a).getBody();
        } else {
            Message b = new Message("failed", "false"); // or any other failure message
            return ResponseEntity.badRequest().body(b).getBody();
        }
    }

    @PostMapping("/login1")
    public String login1(@RequestBody Account loginRequest) {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Account user = accountRepository.findByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            String a = "{\"message\":\"success\"," + "\"status\":\"true\"}";
            return a;
        } else {
            String b = "{\"message\":\"failed\"," + "\"status\":\"false\"}";
                return b;
        }
    }
}
