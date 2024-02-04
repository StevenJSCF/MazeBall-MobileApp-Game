package com.example.Game_Backend.CollaborativeEditing;

import com.example.Game_Backend.Accounts.Account;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "EditingMap")
public class EditingMap {

    @Id
    @Column(name = "editingMap_Id")
    private Long editingMap_Id ;
    private Long host;
    @Lob
    @Column(length = 1000000)
    private String Body;

    @Lob
    @Column(length = 1000000)
    private String WorldData;

    @Lob
    @Column(length = 2139999999)
    private String changes;

    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "user_id")
    private Account editingUser;

    public Account getEditingUser() {
        return editingUser;
    }

    public void setEditingUser(Account editingUser) {
        this.editingUser = editingUser;
    }

    public Long getEditingMap_Id() {
        return editingMap_Id;
    }

    public void setEditingMap_Id(Long editingMap_Id) {
        this.editingMap_Id = editingMap_Id;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getWorldData() {
        return WorldData;
    }

    public void setWorldData(String worldData) {
        WorldData = worldData;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public Long getHost() {return host;}

    public void setHost(Long host) {this.host = host;}
}
