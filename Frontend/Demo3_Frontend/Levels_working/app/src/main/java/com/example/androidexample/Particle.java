package com.example.androidexample;

import org.jbox2d.common.Vec2;

public class Particle {
    private final Vec2 position;
    private final Vec2 velocity;
    private final float radius;

    public Particle(Vec2 position, Vec2 velocity, float radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
    }

    public Vec2 getPosition() {
        return position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public float getRadius() {
        return radius;
    }
}
