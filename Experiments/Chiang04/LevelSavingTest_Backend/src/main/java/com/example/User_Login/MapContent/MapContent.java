package com.example.User_Login.MapContent;

import com.example.User_Login.Accounts.Account;
import com.example.User_Login.Map.Map;

import jakarta.persistence.*;


@Entity
@Table(name = "map_content")
public class MapContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapObjects_id")
    private Long mapObjects_id;
    private Long user_ID;
    @Lob
    @Column(length = 1000000)
    private String Body;

    @Lob
    @Column(length = 1000000)
    private String ListOfBodies;

    @Lob
    @Column(length = 1000000)
    private String WorldData;

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

    public Long getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(Long user_ID) {
        this.user_ID = user_ID;
    }

    public String getBody() {return Body;}

    public void setBody(String body) {Body = body;}

    public String getListOfBodies() {return ListOfBodies;}

    public void setListOfBodies(String listOfBodies) {ListOfBodies = listOfBodies;}

    public String getWorldData() {return WorldData;}

    public void setWorldData(String worldData) {
        WorldData = worldData;
    }
}

