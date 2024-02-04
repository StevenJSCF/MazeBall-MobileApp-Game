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

    @PostMapping("player/post/")
    Player PostPlayerByPath(){
        Player newPlayer = new Player();
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    @GetMapping("/player/{id}")
    Player getUser(@PathVariable Long id) {
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }

    @DeleteMapping("/deletePlayerById/{id}")
    String deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
        return "Deleted Player with id:" + id;
    }


    //gets player's inventory as a Map<K,V>
    //Key is Item ID
    //Value is quantity of item in player's inventory
    @GetMapping("player/{id}/inventory")
    public Map<Long,Integer> getInventory(@PathVariable Long id) {
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getInventory();
    }

    //gets player's money
    @GetMapping("player/{id}/money")
    public int getMoney(@PathVariable Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getMoney();
    }

    //Sets player's money
    @PostMapping("player/{id}/money/{m}")
    public void setMoney(@PathVariable Long id, @PathVariable int m){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.setMoney(m);
        playerRepository.save(p);
    }

    //Adds money to player
    @PutMapping("player/{id}/money/{m}")
    public void addMoney(@PathVariable Long id, @PathVariable int m){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.addMoney(m);
        playerRepository.save(p);
    }

    //Buying an item
    //adds to inventory and subtracts cost from player's money
    @PostMapping("player/{id}/buy/{iid}")
    public void buy(@PathVariable Long id, @PathVariable Long iid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.takeMoney(10);
        p.updateInventory(iid);
        playerRepository.save(p);
    }

    //Adding item to inventory
    //does not affect money
    @PutMapping("player/{id}/addItem/{iid}")
    public void addItem(@PathVariable Long id, @PathVariable Long iid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.updateInventory(iid);
        playerRepository.save(p);
    }

    //Adding friend (1-sided)
    @PostMapping("player/{id}/friends/{fid}")
    public List<Long> addFriend(@PathVariable Long id, @PathVariable Long fid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        List<Long> friends=p.addFriend(fid);
        playerRepository.save(p);
        return friends;
    }

    //Getting friend list
    @GetMapping("player/{id}/friends")
    public List<Long> getFriends(@PathVariable Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getFriends();
    }

    //Getting list of requested friends
    @GetMapping("player/{id}/requests")
    public List<Long> getRequests(@PathVariable Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getPendingFriends();
    }

    //Getting list of received friend invites
    @GetMapping("player/{id}/invites")
    public List<Long> getInvites(@PathVariable Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getFriendInvites();
    }

    //removing a friend (1-sided)
    @DeleteMapping("player/{id}/friends/{fid}")
    public List<Long> delFriend(@PathVariable Long id, @PathVariable Long fid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        List<Long> friends;
        if(p.getFriends().contains(fid))
            friends=p.delFriend(fid);
        else
            friends=p.getFriends();
        playerRepository.save(p);
        return friends;
    }

    //P1 requests to be friends with P2
    //IDs are added to corresponding temp lists
    @PostMapping("player/{id}/fRequest/{fid}")
    public void reqFriend(@PathVariable Long id, @PathVariable Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        p1.requestFriend(fid);
        p2.friendRequested(id);
        playerRepository.save(p1);
        playerRepository.save(p2);
    }

    //P1 accepts a friend request from P2
    //IDs are moved from temp lists to "friends" lists
    @PostMapping("player/{id}/fAccept/{fid}")
    public void acceptFriend(@PathVariable Long id, @PathVariable Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        if(!p2.getPendingFriends().contains(id))
            throw new RuntimeException("Player: "+fid+" does not have record of friend request to player "+id);
        if(!p1.getFriendInvites().contains(fid))
            throw new RuntimeException("Player: "+id+" does not have record of a friend request from player "+fid);
        p1.acceptFriend(fid);
        p2.friendAccepted(id);
        playerRepository.save(p1);
        playerRepository.save(p2);
    }

    //P1 declines P2's friend request
    //IDs are removed from temp lists
    @PostMapping("player/{id}/fDecline/{fid}")
    public void declineFriend(@PathVariable Long id,@PathVariable Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        p1.declineFriend(fid);
        p2.friendDeclined(id);
        playerRepository.save(p1);
        playerRepository.save(p2);
    }

    //P1 retracts their request to be friends with P2
    //IDs are removed from temp lists
    @PostMapping("player/{id}/fRetract/{fid}")
    public void retractFriend(@PathVariable Long id, @PathVariable Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        p1.retractFriend(fid);
        p2.friendRetracted(id);
        playerRepository.save(p1);
        playerRepository.save(p2);
    }

}