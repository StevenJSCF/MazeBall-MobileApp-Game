package com.example.User_Login.MapContent;

import jakarta.persistence.*;

@Entity
@Table(name = "MapContent")
public class MapContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int map_ID;
    private int user_ID;
    private String map_content;

    public int getMap_ID() {
        return map_ID;
    }

    public void setMap_ID(int map_ID) {
        this.map_ID = map_ID;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getMap_content() {
        return map_content;
    }

    public void setMap_content(String map_content) {
        this.map_content = map_content;
    }

}

