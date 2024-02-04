package com.example.User_Login.Accounts;

import com.example.User_Login.MapContent.MapContent;
import com.example.User_Login.PlayerMapInfo.Map;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id ;
    private String username;
    private String password;
    private String user_type;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Map> maps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MapContent> mapsContent;

    public Long getId() {
        return user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() { return user_type;}

    public void setUser_type(String user_type) { this.user_type = user_type; }

    // Add this method to your Account class
    public List<Map> getMaps() {
        return maps;
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }

}





