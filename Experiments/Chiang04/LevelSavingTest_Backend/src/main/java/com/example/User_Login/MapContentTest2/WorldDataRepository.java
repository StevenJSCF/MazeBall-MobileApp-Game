package com.example.User_Login.MapContentTest2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldDataRepository extends JpaRepository<WorldData, Long> {
    // Custom query methods, if needed
}