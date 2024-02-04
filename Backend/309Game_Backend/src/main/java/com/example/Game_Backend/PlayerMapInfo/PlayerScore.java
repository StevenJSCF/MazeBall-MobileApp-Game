package com.example.Game_Backend.PlayerMapInfo;

public class PlayerScore {
    private final int playerId;
    private final int highScore;

    public PlayerScore(int playerId, int highScore) {
        this.playerId = playerId;
        this.highScore = highScore;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getHighScore() {
        return highScore;
    }


}