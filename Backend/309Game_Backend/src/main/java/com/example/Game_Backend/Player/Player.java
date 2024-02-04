package com.example.Game_Backend.Player;

import com.example.Game_Backend.Items.Item;
import com.example.Game_Backend.Items.ItemRepository;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="player")
public class Player {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int money;

    @ElementCollection
    @CollectionTable(name = "player_friends", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "friends")
    private List<Long> friends;

    //temp list (sending side)
    @ElementCollection
    @CollectionTable(name = "player_pending_friends", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "pending_friends")
    private List<Long> pendingFriends;

    //temp list (receiving side)
    @ElementCollection
    @CollectionTable(name = "player_friend_invites", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "friend_invites")
    private List<Long> friendInvites;

    @ElementCollection
    @CollectionTable(name = "player_blocked_players", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "blocked_players")
    private List<Long> blockedPlayers;

    @ElementCollection
    @CollectionTable(name = "player_inventory", joinColumns = @JoinColumn(name = "player_id"))
    private Map<Long,Integer> inventory;

    @Column
    private Integer imageId;

    //@OneToOne(mappedBy = "player")
    //private Account account;

    public Player(){
        money=0;
        friends=new ArrayList<>();
        pendingFriends=new ArrayList<>();
        friendInvites=new ArrayList<>();
        blockedPlayers=new ArrayList<>();
        inventory=new HashMap<>();
        imageId=1;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}
    public int getMoney(){return money;}
    public int addMoney(int newMoney){this.money+=newMoney;return money;}
    public int setMoney(int money){this.money=money;return money;}
    public int setImageId(int id){imageId=id;return imageId;}
    public int getImageId(){return imageId;}
    public int takeMoney(int outMoney){
        if(outMoney>money){
            throw new RuntimeException("Player only has: "+money+".\nNot enough for "+outMoney);
        }else {money-=outMoney;}
        return money;
    }

    // adds all items to the inventory with a count of 0 for each item.
    public void setupInventory(ItemRepository itemRepo){
        List<Item> itemList=itemRepo.findAll();
        for (Item i:itemList) {
            inventory.putIfAbsent(i.getId(),0);
        }
    }

    public Map<Long,Integer> setInventory(Map<Long,Integer> inv){
        inventory=new HashMap<Long,Integer>();
        inventory.putAll(inv);
        return inventory;
    }

    public Map<Long,Integer> getInventory(){return inventory;}

    public void updateInventory(Long iid){
        if(inventory.containsKey(iid)){
            inventory.put(iid,inventory.get(iid)+1);
        }else{
            inventory.put(iid,1);
        }
    }

    public void useAnItem(Long iid){
        if(inventory.containsKey(iid)){
            if(inventory.get(iid)>0)
                inventory.put(iid,inventory.get(iid)-1);
        }
    }

    public List<Long> setFriends(List<Long> friends){
        this.friends=new ArrayList<>(friends);
        return friends;
    }

    public List<Long> addFriend(Long Friend){
        if(!friends.contains(Friend))
            friends.add(Friend);
        return friends;
    }

    public List<Long> delFriend(Long Friend){
        friends.remove(Friend);
        return friends;
    }

    public List<Long> getFriends(){return friends;}

    public List<Long> setFriendInvites(List<Long> fInvites){
        friendInvites=new ArrayList<>(fInvites);
        return friendInvites;
    }

    public List<Long> getFriendInvites(){return friendInvites;}

    public List<Long> setPendingFriends(List<Long> fPending){
        pendingFriends=new ArrayList<>(fPending);
        return pendingFriends;
    }

    public List<Long> getPendingFriends(){return pendingFriends;}

    public void requestFriend(Long Friend){pendingFriends.add(Friend);}

    public List<Long> friendAccepted(Long Friend){
        pendingFriends.remove(Friend);
        if(friends==null)
            friends=new ArrayList<>();
        if(!friends.contains(Friend))
            friends.add(Friend);
        return friends;
    }

    public void retractFriend(Long Friend){pendingFriends.remove(Friend);}

    public void friendDeclined(Long Friend){pendingFriends.remove(Friend);}

    public void friendRequested(Long Friend){
        if(friendInvites==null)
            friendInvites=new ArrayList<>();
        friendInvites.add(Friend);
    }

    public List<Long> acceptFriend(Long Friend){
        friendInvites.remove(Friend);
        if(!friends.contains(Friend))
            friends.add(Friend);
        return friends;
    }

    public void declineFriend(Long Friend){friendInvites.remove(Friend);}

    public void friendRetracted(Long Friend){friendInvites.remove(Friend);}

    public List<Long> block(Long player){
        blockedPlayers.add(player);
        return blockedPlayers;
    }

    public List<Long> unblock(Long player){
        blockedPlayers.remove(player);
        return blockedPlayers;
    }

    public List<Long> getBlockedPlayers(){
        return blockedPlayers;
    }

    public List<Long> setBlockedPlayers(List<Long> newBlocked){
        blockedPlayers=newBlocked;
        return blockedPlayers;
    }

}
