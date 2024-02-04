package com.example.User_Login.PlayerMapInfo;

import com.example.User_Login.MapContent.MapContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends  JpaRepository<Map, Long> {
//    Map findByUserId(Long userId);

}
