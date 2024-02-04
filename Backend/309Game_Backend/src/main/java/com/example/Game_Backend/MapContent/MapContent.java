package com.example.Game_Backend.MapContent;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.PlayerMapInfo.Map;
import jakarta.persistence.*;


@Entity
@Table(name = "map_content")
public class MapContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapObjects_id")
    private Long mapObjects_id;
    @Lob
    @Column(length = 1000000)
    private String Body;

    @Lob
    @Column(length = 1000000)
    private String WorldData;

    private int mapNumber;

    @ManyToOne
    @JoinColumn(name = "map_id")
    private Map map;

    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "user_id")
    private Account user;

    public Long getMap_ID() {
        return mapObjects_id;
    }

    public void setMap_ID(Long map_ID) {
        this.mapObjects_id = map_ID;
    }

    public String getBody() {return Body;}

    public void setBody(String body) {Body = body;}

    public String getWorldData() {return WorldData;}

    public void setWorldData(String worldData) {
        WorldData = worldData;
    }

    public int getMapNumber() {return mapNumber;}

    public void setMapNumber(int mapNumber) {this.mapNumber = mapNumber;}

    public void setUser(Account user) {
        this.user = user;
    }

    public Account getUser(){
        return user;
    }


}

