package com.example.Game_Backend.Player;

import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.Items.ItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Player API", description = "HTTP request mappings related to Players")
public class PlayerController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    AccountRepository accountRepository;

    /*
     *  GET MAPPINGS:
    */

    @Operation(summary = "Get all players", description = "Returns a list containing all Players.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Failure")
    })
    @GetMapping("player/all")
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping("player/all/names")
    public Map<Long,String> getAllNames(){
        List<Player> players=playerRepository.findAll();
        Map<Long,String> names=new HashMap<>();
        players.forEach(player ->
                names.put(player.getId(),
                        accountRepository.findById(player.getId())
                                .orElseThrow(RuntimeException::new)
                                .getUsername()
                )
        );
        return names;
    }

    @Operation(summary = "Get Player by ID", description = "returns the Player object represented by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("getPlayerById/{id}")
    public Player fetchDetailsById(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found for id: " + id));
    }

    @Operation(summary = "Get Player by ID", description = "returns the Player object represented by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("/player/{id}")
    public Player getUser(@PathVariable @Parameter(description = "Player id", example = "1") Long id) {
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
    }
    
    @Operation(summary = "Gets Player {id}'s money.", description = "returns the value of player.money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/money")
    public int getMoney(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getMoney();
    }

    @Operation(summary = "Gets the inventory of Player {id}", description = "returns the player's inventory as a Map<K,V>.\n" +
            "The key is the Item ID, the value at each key is the quantity of the item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/inventory")
    public Map<Long,Integer> getInventory(@PathVariable @Parameter(description = "Player id", example = "1") Long id) {
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getInventory();
    }

    //FRIENDS

    @Operation(summary = "Returns a list of the IDs of the Player's friends")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/friends")
    public List<Long> getFriends(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        return p.getFriends();
    }

    @Operation(summary = "returns a list of the names of the Player's friends")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/friends/names")
    public List<String> getFriendNames(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        List<Long> friendIDs=p.getFriends();
        List<String> friends=new ArrayList<>();
        for(Long ID:friendIDs){
            friends.add(accountRepository.findById(ID).
                    orElseThrow(RuntimeException::new).
                    getUsername()
            );
        }
        return friends;
    }

    @Operation(summary = "returns a list of the IDs that Player has requested to be friends with.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/requests")
    public List<Long> getRequests(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getPendingFriends();
    }

    @Operation(summary = "returns a list of the IDs that have requested to be friends with Player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/invites")
    public List<Long> getInvites(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return playerRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getFriendInvites();
    }

    @Operation(summary = "returns the username of Player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @GetMapping("player/{id}/name")
    public String getName(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return accountRepository.findById(id).
                orElseThrow(RuntimeException::new).
                getUsername();
    }

    @GetMapping("player/{id}/blocked")
    public List<Long> getBlocked(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return playerRepository.findById(id).orElseThrow(RuntimeException::new).getBlockedPlayers();
    }

    @GetMapping("player/{id}/image")
    public int getImage(@PathVariable @Parameter(description = "Player id", example = "1") Long id){
        return playerRepository.findById(id).orElseThrow(RuntimeException::new).getImageId();
    }

    /*
     *  POST MAPPINGS:
     */

    @Operation(summary = "creates a Player object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "failure")
    })
    @PostMapping("player/post/")
    public Player PostPlayerByPath(){
        return playerRepository.save(new Player());
    }

    @Operation(summary = "sets a Player's money", description = "sets Player {id}'s money to {m}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/money/{m}")
    public void setMoney(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                         @PathVariable @Parameter(description = "int value that Player's money will be set to", example = "100") int m){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.setMoney(m);
        playerRepository.save(p);
    }

    @Operation(summary = "Buys an item", description = "Adds item {iid} to Player {id}'s inventory\n+" +
            "and subtracts the cost of item from the player's money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/buy/{iid}")
    public void buy(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                    @PathVariable @Parameter(description = "item id", example = "1") Long iid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.takeMoney(10);
        p.updateInventory(iid);
        playerRepository.save(p);
    }

    @Operation(summary = "Adds a friend", description = "Adds a friend {fid}, skipping the approval process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/friends/{fid}")
    public List<Long> addFriend(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                                @PathVariable @Parameter(description = "Friend id", example = "2") Long fid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        if(!playerRepository.findById(fid).orElseThrow(RuntimeException::new).getBlockedPlayers().contains(fid)) {
            List<Long> friends = p.addFriend(fid);
            playerRepository.save(p);
            return friends;
        }else{
            return p.getFriends();
        }
    }

    @Operation(summary = "Makes a friend request", description = "Player {id} requests to be friends with Player {fid}\n" +
            "Player IDs are added to corresponding lists in both Player objects awaiting approval by the requested Player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/fRequest/{fid}")
    public void reqFriend(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                          @PathVariable @Parameter(description = "Friend id", example = "2") Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        if(!p2.getBlockedPlayers().contains(id)) {
            p1.requestFriend(fid);
            p2.friendRequested(id);
            playerRepository.save(p1);
            playerRepository.save(p2);
            FriendNotifications.onRequest(id, fid);
        }
    }

    @Operation(summary = "Accepts a friend request.", description = "Player {id} accepts friend request from Player {fid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/fAccept/{fid}")
    public void acceptFriend(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                             @PathVariable @Parameter(description = "Friend id", example = "2") Long fid){
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

    @Operation(summary = "Declines a friend request.", description = "Player {id} declines friend request from Player {fid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/fDecline/{fid}")
    public void declineFriend(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                              @PathVariable @Parameter(description = "Friend id", example = "2") Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        p1.declineFriend(fid);
        p2.friendDeclined(id);
        playerRepository.save(p1);
        playerRepository.save(p2);
    }

    @Operation(summary = "Cancels a friend request", description = "Player {id} cancels their friend request to Player {fid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PostMapping("player/{id}/fRetract/{fid}")
    public void retractFriend(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                              @PathVariable @Parameter(description = "Friend id", example = "2") Long fid){
        Player p1=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        Player p2=playerRepository.findById(fid).
                orElseThrow(RuntimeException::new);
        p1.retractFriend(fid);
        p2.friendRetracted(id);
        playerRepository.save(p1);
        playerRepository.save(p2);
    }

    @PostMapping("player/{id}/image/{iid}")
    public int setImageId(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                          @PathVariable @Parameter(description = "Image id", example = "1") int iid){
        Player p=playerRepository.findById(id).orElseThrow(RuntimeException::new);
        p.setImageId(iid);
        playerRepository.save(p);
        return iid;
    }

    @PostMapping("player/{id}/block/{bid}")
    public List<Long> blockPlayer(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                                  @PathVariable @Parameter(description = "Id to block", example = "2") Long bid){
        Player p=playerRepository.findById(id).orElseThrow(RuntimeException::new);
        playerRepository.save(p);
        return p.block(bid);
    }


    /*
     *  PUT MAPPINGS:
    */

    @Operation(summary = "adds money {m} to Player {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PutMapping("player/{id}/money/{m}")
    public void addMoney(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                         @PathVariable @Parameter(description = "int value that will be added to Player {id}'s money", example = "100") int m){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.addMoney(m);
        playerRepository.save(p);
    }

    //Adding item to inventory
    //does not affect money
    @Operation(summary = "adds item {iid} to player {id}'s inventory.", description = "adds item without affecting Player{id}'s money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @PutMapping("player/{id}/addItem/{iid}")
    public void addItem(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                        @PathVariable @Parameter(description = "item id", example = "1") Long iid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.updateInventory(iid);
        playerRepository.save(p);
    }

    @PutMapping("player/{id}/useItem/{iid}")
    public void useItem(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                        @PathVariable @Parameter(description = "item id", example = "1") Long iid){
        Player p=playerRepository.findById(id).
                orElseThrow(RuntimeException::new);
        p.useAnItem(iid);
        playerRepository.save(p);
    }

    @PutMapping("player/{id}/addItem/{iid}/sell")
    public void sellItem(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                         @PathVariable @Parameter(description = "item id", example = "1") Long iid){
        Player p=playerRepository.findById(id).orElseThrow(RuntimeException::new);
        Map<Long, Integer> inv=p.getInventory();
        if(inv.containsKey(iid)){
            inv.replace(iid,inv.get(iid)-1);
            p.addMoney(itemRepository.findById(iid).orElseThrow(RuntimeException::new).getPrice());
        }else{
            throw new RuntimeException("Item not found exception");
        }
        p.setInventory(inv);
        playerRepository.save(p);
    }

    /*
     * DELETE MAPPINGS:
    */

    @Operation(summary = "deletes player {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @DeleteMapping("/deletePlayerById/{id}")
    public String deletePlayer(@PathVariable @Parameter(description = "Player id", example = "1") Long id) {
        playerRepository.deleteById(id);
        return "Deleted Player with id:" + id;
    }

    @Operation(summary = "removes friend {fid} from Player {id}'s friend list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "404", description = "Player not found for id: + id")
    })
    @DeleteMapping("player/{id}/friends/{fid}")
    public List<Long> delFriend(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                                @PathVariable @Parameter(description = "Friend id", example = "2") Long fid){
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

    @DeleteMapping("player/{id}/block/{bid}")
    public List<Long> unblockPlayer(@PathVariable @Parameter(description = "Player id", example = "1") Long id,
                                  @PathVariable @Parameter(description = "Id to unblock", example = "2") Long bid){
        Player p=playerRepository.findById(id).orElseThrow(RuntimeException::new);
        playerRepository.save(p);
        return p.unblock(bid);
    }

}