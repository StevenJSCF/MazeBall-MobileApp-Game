package com.example.Game_Backend.MapContent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapContentRepository extends JpaRepository<MapContent, Long> {

    MapContent findByUserId(Long userId);

}
