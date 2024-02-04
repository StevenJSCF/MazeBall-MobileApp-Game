package com.example.User_Login.Map;

import com.example.User_Login.Accounts.Account;

import com.example.User_Login.MapContent.MapContent;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "map")
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long map_id;
    private int playerMap_score;
    private String creator;
    private int playerMap_highScore;
    private String highScore_holder;
    private int xp_granted;
    private int currency_granted;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id")
//    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    @OneToMany(mappedBy = "map")
    private List<MapContent> mapContents;


    public Long getMap_ID() {return map_id;}

    public void setMap_ID(Long map_ID) {this.map_id = map_ID;}

    public int getMap_score() {return playerMap_score;}

    public void setMap_score(int map_score) {this.playerMap_score = map_score;}

    public String getCreator() {return creator;}

    public void setCreator(String creator) {this.creator = creator;}

    public int getPlayer_HighScore() {return playerMap_highScore;}

    public void setPlayer_HighScore(int player_HighScore) {this.playerMap_highScore = player_HighScore;}

    public String getHighScore_Holder() {return highScore_holder;}

    public void setHighScore_Holder(String highScore_Holder) {highScore_holder = highScore_Holder;}

    public int getXp_granted() {return xp_granted;}

    public void setXp_granted(int xp_granted) {this.xp_granted = xp_granted;}

    public int getCurrency_granted() {return currency_granted;}

    public void setCurrency_granted(int currency_granted) {this.currency_granted = currency_granted;}


}




