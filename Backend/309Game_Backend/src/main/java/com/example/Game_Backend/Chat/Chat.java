package com.example.Game_Backend.Chat;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private Set<String> players;

    @OneToMany(mappedBy = "chat",fetch = FetchType.EAGER)
    private List<Message> messages;

    public Chat(String p1,String p2){
        players=new HashSet<>(Arrays.asList(p1,p2));
        messages=new ArrayList<>();
    }

    public Chat(){
        players=new HashSet<>();
        messages=new ArrayList<>();
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        messages.add(message);
    }


}
