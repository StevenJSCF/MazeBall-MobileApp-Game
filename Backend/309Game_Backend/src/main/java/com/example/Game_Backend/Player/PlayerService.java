package com.example.Game_Backend.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepo;

    public Player getPlayerDetailsById(Long id){
        Optional<Player> playerOptional = playerRepo.findById(id);

        if (playerOptional.isPresent()) {
            return playerOptional.get(); // Extract the player from Optional
        } else {
            // Handle the case when the player is not found, e.g., by throwing an exception or returning null.
            throw new RuntimeException("Player not found for id: " + id);
        }
    }
}
