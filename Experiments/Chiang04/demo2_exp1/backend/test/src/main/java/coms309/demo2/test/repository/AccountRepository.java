package coms309.demo2.test.repository;


import coms309.demo2.test.module.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {



}
