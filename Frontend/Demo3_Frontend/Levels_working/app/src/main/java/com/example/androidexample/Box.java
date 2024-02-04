package com.example.androidexample;

import org.jbox2d.common.Vec2;

public class Box {
    private final Vec2 position;
    private final float width;
    private final float height;

    public Box(Vec2 position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    // Implement a method to check if a ball falls inside the box
    public boolean isBallInside(Ball ball) {
        float ballX = ball.getPosition().x;
        float ballY = ball.getPosition().y;

        return ballX >= position.x && ballX <= position.x + width &&
                ballY >= position.y && ballY <= position.y + height;
    }
}


