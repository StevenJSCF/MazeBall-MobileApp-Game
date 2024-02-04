package com.example.androidexample;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

public class ParticleSystem {
    private final World world;
    private final ArrayList<Particle> particles;
    private final float gravity;  // Gravity value


    public ParticleSystem(World world , float gravity) {
        this.world = world;
        this.particles = new ArrayList<>();
        this.gravity = gravity;
    }

    public void createParticle(Vec2 position, Vec2 velocity, float radius ) {
        Particle particle = new Particle(position, velocity, radius);
        particles.add(particle);
    }

    public void update(float timeStep) {
        // Simulate particle motion
        for (Particle particle : particles) {
            Vec2 newPosition = particle.getPosition().add(particle.getVelocity().mul(timeStep));

            // Apply gravity to the particle's velocity
            Vec2 gravityForce = new Vec2(0, -gravity); // Negative Y value to simulate downward gravity
            Vec2 newVelocity = particle.getVelocity().add(gravityForce.mul(timeStep));
            particle.getVelocity().set(newVelocity);

            particle.getPosition().set(newPosition);
        }
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

}
