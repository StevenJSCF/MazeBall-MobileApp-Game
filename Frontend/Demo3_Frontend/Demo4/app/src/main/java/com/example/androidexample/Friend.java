package com.example.androidexample;

public class Friend {
    private String friendUsername;
    private String friendId;

    public Friend(String friendUsername, String friendId) {
        this.friendUsername = friendUsername;
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public String getFriendId() {
        return friendId;
    }

    @Override
    public String toString() {
        return friendUsername; // ArrayAdapter uses this for displaying the item.
    }
}
