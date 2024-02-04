package com.example.Game_Backend.module;

public class NumMessage {

    private int message;
    private String status;
    public NumMessage(int message, String status) {
        this.message = message;
        this.status = status;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
