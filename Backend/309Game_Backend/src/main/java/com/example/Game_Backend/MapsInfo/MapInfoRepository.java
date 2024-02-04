package com.example.Game_Backend.MapsInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapInfoRepository extends JpaRepository<MapInfo, Long> {

}
