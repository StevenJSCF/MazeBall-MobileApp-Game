package com.example.User_Login.Map;

import com.example.User_Login.Accounts.Account;
import jakarta.persistence.*;

@Entity
@Table(name = "Maps")
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int map_ID;
    private int map_score;
    private String creator;
    private int player_HighScore;
    private String HighScore_Holder;
    private int xp_granted;
    private int currency_granted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ID")
    private Account account;

    public int getMap_ID() {return map_ID;}

    public void setMap_ID(int map_ID) {this.map_ID = map_ID;}

    public int getMap_score() {return map_score;}

    public void setMap_score(int map_score) {this.map_score = map_score;}

    public String getCreator() {return creator;}

    public void setCreator(String creator) {this.creator = creator;}

    public int getPlayer_HighScore() {return player_HighScore;}

    public void setPlayer_HighScore(int player_HighScore) {this.player_HighScore = player_HighScore;}

    public String getHighScore_Holder() {return HighScore_Holder;}

    public void setHighScore_Holder(String highScore_Holder) {HighScore_Holder = highScore_Holder;}

    public int getXp_granted() {return xp_granted;}

    public void setXp_granted(int xp_granted) {this.xp_granted = xp_granted;}

    public int getCurrency_granted() {return currency_granted;}

    public void setCurrency_granted(int currency_granted) {this.currency_granted = currency_granted;}

}




