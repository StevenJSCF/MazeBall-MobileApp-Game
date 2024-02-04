package coms309.demo2.test.service;

import coms309.demo2.test.module.Account;
import coms309.demo2.test.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepo;

    public Account getAccountDetailsById(Long id) {
        Optional<Account> accountOptional = accountRepo.findById(id);

        if (accountOptional.isPresent()) {
            return accountOptional.get(); // Extract the Account from Optional
        } else {
            // Handle the case when the account is not found, e.g., by throwing an exception or returning null.
            throw new RuntimeException("Account not found for id: " + id);
        }
    }
}
