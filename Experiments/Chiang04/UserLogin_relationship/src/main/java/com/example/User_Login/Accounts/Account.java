package com.example.User_Login.Accounts;

import com.example.User_Login.Map.Map;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String user_type;

    @OneToMany(targetEntity = Map.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_map_fk", referencedColumnName = "id")
    private List<Map> maps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}





