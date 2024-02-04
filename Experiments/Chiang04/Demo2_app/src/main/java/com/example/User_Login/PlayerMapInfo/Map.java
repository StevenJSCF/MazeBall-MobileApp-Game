package com.example.User_Login.PlayerMapInfo;

import com.example.User_Login.Accounts.Account;

import com.example.User_Login.MapContent.MapContent;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "map",uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "map_number"}))
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long PlayerMapInfo_id;

    @Column(name = "map_number")
    private int mapNumber;
    private int playerMap_highScore;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "user_id")
    private Account account;


    public Long getPlayerMapInfo_id() {
        return PlayerMapInfo_id;
    }

    public void setPlayerMapInfo_id(Long playerMapInfo_id) {
        PlayerMapInfo_id = playerMapInfo_id;
    }

    public int getPlayer_HighScore() {return playerMap_highScore;}

    public void setPlayer_HighScore(int player_HighScore) {this.playerMap_highScore = player_HighScore;}

    public int getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
    }

    public void setUser(Account user) {
        this.account = user;
    }

    public Account getUser(){
        return account;
    }
}




