package com.example.Game_Backend.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByPlayersContainsAndPlayersContains(String username1, String username2);
    boolean existsChatsByPlayersContainsAndPlayersContains(String username1, String username2);
}
