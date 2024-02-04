package com.example.Game_Backend.PlayerMapInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MapRepository extends JpaRepository<Map, Long> {
//Map findByUserIdAndMapNumber(int playerMap_highScore, int map_number);
//Map findByUserId(Long userId);
Map findByAccountId(Long userId);
    Optional<Map> findByAccountIdAndMapNumber(Long userId, int mapNumber);

}
