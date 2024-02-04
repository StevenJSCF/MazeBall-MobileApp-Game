package com.example.User_Login.playerStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping("player/all")
    List<Player> GetAllPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping("getPlayerById/{id}")
    Player fetchDetailsById(@PathVariable Long id){
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found for id: " + id));
    }

    @PostMapping("player/post/{u}/{p}")
    Player PostPlayerByPath(@PathVariable String u, @PathVariable String p){
        Player newPlayer = new Player();
        newPlayer.setUsername(u);
        newPlayer.setPassword(p);
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    @GetMapping("/player/{id}")
    Player getUser(@PathVariable Long id) {
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

    // Updating the player username
    @PutMapping("/updatePlayerUsernameById/{id}")
    Player updateUsername(@RequestBody Player updatedPlayer, @PathVariable Long id) {
        Player oldUser = playerRepository.findById(id).orElseThrow(() -> new RuntimeException("Player not found for id: " + id));
        oldUser.setUsername(updatedPlayer.getUsername());
        playerRepository.save(oldUser);
        return oldUser;
    }

    // Updating the player password
    @PutMapping("/updatePlayerPasswordById/{id}")
    Player updatePassword(@RequestBody Player updatedPassword, @PathVariable Long id) {
        Player oldPassword = playerRepository.findById(id).orElseThrow(() -> new RuntimeException("Player not found for id: " + id));
        oldPassword.setPassword(updatedPassword.getPassword());
        playerRepository.save(oldPassword);
        return oldPassword;
    }

    @DeleteMapping("/deletePlayerById/{id}")
    String deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
        return "Deleted Player with id:" + id;
    }

    @GetMapping("/login/{username}/{password}")
    public boolean login(@PathVariable String username, @PathVariable String password) {
        // Authenticate the user based on the provided username and password
        // Check if the username and password match a user account in the database

        Player user = playerRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    @GetMapping("player/{id}/inventory")
    public Map<Long,Integer> getInventory(@PathVariable Long id) {
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getInventory();
    }

    @GetMapping("player/{id}/money")
    public int getMoney(@PathVariable Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getMoney();
    }

    @PostMapping("player/{id}/money/{m}")
    public void setMoney(@PathVariable Long id, @PathVariable int m){
        playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                setMoney(m);
    }

    @PutMapping("player/{id}/money/{m}")
    public void addMoney(@PathVariable Long id, @PathVariable int m){
        playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                addMoney(m);
    }

    @PostMapping("player/{id}/buy/{iid}")
    public void buy(@PathVariable Long id, @PathVariable Long iid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.takeMoney(10);
        p.updateInventory(iid);
    }

    @PutMapping("player/{id}/addItem/{iid}")
    public void addItem(@PathVariable Long id, @PathVariable Long iid){
        playerRepository.findById(id).
                orElseThrow(RuntimeException::new)
                .updateInventory(iid);
    }
}
