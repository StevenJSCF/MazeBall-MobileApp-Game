package com.example.User_Login.playerStore;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int money;

    @ElementCollection
    private List<Long> friends;

    //temp list (sending side)
    @ElementCollection
    private List<Long> pendingFriends;

    //temp list (receiving side)
    @ElementCollection
    private List<Long> friendInvites;

    @ElementCollection
    private Map<Long,Integer> inventory;

    public int getId() {return id;}
    public void setId(int id) {this.id=id;}
    public int getMoney(){return money;}
    public int addMoney(int newMoney){this.money+=newMoney;return money;}
    public int setMoney(int money){this.money=money;return money;}
    public int takeMoney(int outMoney){
        if(outMoney>money){
            throw new RuntimeException("Player only has: "+money+".\nNot enough for "+outMoney);
        }else {money-=outMoney;}
        return money;
    }

    //TODO possibly implement for admin account
    public void setInventory(ItemRepository itemRepo){
        List<Item> itemList=itemRepo.findAll();
        for (Item i:itemList) {
            inventory.putIfAbsent(i.getId(),0);
        }
    }
    public Map<Long,Integer> getInventory(){return inventory;}
    public void updateInventory(Long iid){
        if(inventory.containsKey(iid)){
            inventory.put(iid,inventory.get(iid)+1);
        }else{
            inventory.put(iid,1);
        }
    }

    //TODO possibly implement for admin account
    public List<Long> setFriends(List<Long> friends){
        this.friends=friends;
        return friends;
    }

    public List<Long> addFriend(Long Friend){
        friends.add(Friend);
        return friends;
    }

    public List<Long> delFriend(Long Friend){
        friends.remove(Friend);
        return friends;
    }

    public List<Long> getFriends(){return friends;}

    public List<Long> getFriendInvites(){return friendInvites;}

    public List<Long> getPendingFriends(){return pendingFriends;}

    public void requestFriend(Long Friend){pendingFriends.add(Friend);}

    public List<Long> friendAccepted(Long Friend){
        pendingFriends.remove(Friend);
        friends.add(Friend);
        return friends;
    }

    public void retractFriend(Long Friend){pendingFriends.remove(Friend);}

    public void friendDeclined(Long Friend){pendingFriends.remove(Friend);}

    public void friendRequested(Long Friend){friendInvites.add(Friend);}

    public List<Long> acceptFriend(Long Friend){
        friendInvites.remove(Friend);
        friends.add(Friend);
        return friends;
    }

    public void declineFriend(Long Friend){friendInvites.remove(Friend);}

    public void friendRetracted(Long Friend){friendInvites.remove(Friend);}


}
