package com.example.User_Login.module;

public class Message {
    private String message;
    private String status;
    public Message(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}