package com.example.User_Login.playerStore;

import jakarta.persistence.*;

@Entity
@Table(name="store")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;

    public Long getId(){return id;}
    public void setId(Long id) {this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
