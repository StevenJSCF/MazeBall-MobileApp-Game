package com.example.User_Login.MapContent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MapContentRepository extends JpaRepository<MapContent, Long> {

    MapContent findByUserId(Long userId);

}
