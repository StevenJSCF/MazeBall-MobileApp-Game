package com.example.Game_Backend.Chat;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent=new Date();

    @ManyToOne
    private Chat chat;

	public Message() {}
	
	public Message(String userName, String content,Chat chat) {
		this.userName = userName;
		this.content = content;
        this.chat=chat;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName=userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content=content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent=sent;
    }

}
