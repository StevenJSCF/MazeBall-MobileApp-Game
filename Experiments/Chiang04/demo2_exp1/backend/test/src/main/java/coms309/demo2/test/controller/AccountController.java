package coms309.demo2.test.controller;

import coms309.demo2.test.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coms309.demo2.test.module.Account;

import java.util.List;
import java.util.UUID;

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
    @PostMapping("account/post/{u}/{p}")
    Account PostAccountByPath(@PathVariable String u, @PathVariable String p){
        Account newAccount = new Account();
        newAccount.setUsername(u);
        newAccount.setPassword(p);
        accountRepository.save(newAccount);
        return newAccount;
    }

    //This post an account with JSON on postman
    @PostMapping("account/post")
    Account PostAccountByPath(@RequestBody Account newAccount){
        accountRepository.save(newAccount);
        return newAccount;
    }

    @GetMapping("/account/{id}")
    Account getUser(@PathVariable Long id) {
        return accountRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

//NEED TO WORK ON THIS :

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
}
