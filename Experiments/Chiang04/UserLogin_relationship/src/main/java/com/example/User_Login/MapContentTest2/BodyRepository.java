package com.example.User_Login.MapContentTest2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyRepository extends JpaRepository<CircleBody, Long> {
    // Custom query methods, if needed
}
