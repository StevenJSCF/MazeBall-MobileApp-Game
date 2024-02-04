package com.example.myapplication;

public class Message {

    private String content;
    private boolean isCurrentUser;

    public Message(String content, boolean isCurrentUser) {
        this.content = content;
        this.isCurrentUser = isCurrentUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    // Optionally, setters can be added here if needed.
}

