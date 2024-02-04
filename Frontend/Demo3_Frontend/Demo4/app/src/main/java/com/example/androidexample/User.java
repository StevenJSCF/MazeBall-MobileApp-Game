package com.example.androidexample;

public class User {
    private String username;
    private String userId; // Assuming you have a user ID

    public User(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {

        return username;
    }

    public String getUserId() {

        return userId;
    }

    @Override
    public String toString() {

        return username;
    }
}

