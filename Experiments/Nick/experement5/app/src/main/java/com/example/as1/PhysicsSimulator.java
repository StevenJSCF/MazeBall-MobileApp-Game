package com.example.as1;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.collision.shapes.CircleShape;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {
    private World world;
    private List<Body> bodies;

    public PhysicsSimulator() {
        // Create a new physics world with gravity
        world = new World(new Vec2(0f, 9.8f)); // Gravity in m/s^2, adjusted for the direction
        bodies = new ArrayList<>();
        createBall(100, 0);
        createBall(300, 0);

    }

    public void createBall(float x, float y) {
        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC; // Dynamic body responds to forces (e.g., gravity)
        bodyDef.position.set(x, y); // Initial position

        // Create the body in the world
        Body ballBody = world.createBody(bodyDef);

        // Define the fixture (shape and density)
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(100.0f); // Radius of the circle (adjust as needed)
        fixtureDef.shape = circleShape;
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.3f;

        // Attach the fixture to the body
        ballBody.createFixture(fixtureDef);

        // Add the created body to the list
        bodies.add(ballBody);
    }

    public void update(float elapsedTime) {
        // Update the physics simulation
        world.step(elapsedTime, 6, 2); // Perform a time step (adjust parameters as needed)
    }

    public List<Body> getBodies() {
        return bodies;
    }
}
