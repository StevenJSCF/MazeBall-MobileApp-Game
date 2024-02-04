package com.example.User_Login.MapsInfo;


import jakarta.persistence.*;

@Entity
@Table(name = "MapInfo")
public class MapInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long MapInfo_Id;
    private int mapCurrency_granted;
    private int map_highestScore;

    private String highScoreHolder;
    private String creator;

    public Long getMapInfo_Id() {return MapInfo_Id;}

    public void setMapInfo_Id(Long mapInfo_Id) {MapInfo_Id = mapInfo_Id;}

    public int getMapCurrency_granted() {return mapCurrency_granted;}

    public void setMapCurrency_granted(int mapCurrency_granted) {this.mapCurrency_granted = mapCurrency_granted;}

    public int getMap_highestScore() {return map_highestScore;}

    public void setMap_highestScore(int map_highestScore) {this.map_highestScore = map_highestScore;}

    public String getCreator() {return creator;}

    public void setCreator(String creator) {this.creator = creator;}

    public String getHighScoreHolder() {
        return highScoreHolder;
    }

    public void setHighScoreHolder(String highScoreHolder) {
        this.highScoreHolder = highScoreHolder;
    }
}
