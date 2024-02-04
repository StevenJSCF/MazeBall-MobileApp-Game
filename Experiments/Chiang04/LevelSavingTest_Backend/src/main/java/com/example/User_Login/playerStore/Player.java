package com.example.User_Login.playerStore;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private int money=10000;

    @ElementCollection
    private Map<Long,Integer> inventory;

    public int getId() {return id;}
    public void setId(int id) {this.id=id;}
    public String getUsername() {return username;}
    public void setUsername(String username){this.username=username;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}
    public int getMoney(){return money;}
    public void addMoney(int newMoney){this.money+=newMoney;}
    public void setMoney(int money){this.money=money;}
    public void takeMoney(int outMoney){
        if(outMoney>money){
            throw new RuntimeException("Player only has: "+money+".\nNot enough for "+outMoney);
        }else {money-=outMoney;}
    }

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

}
