package com.example.as1;

import org.jbox2d.collision.shapes.PolygonShape;
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
        world = new World(new Vec2(0f, 10)); // Gravity in m/s^2, adjusted for the direction
        bodies = new ArrayList<>();

       // world.

       createGround(450, 1000, 800, 200);

        createBall(5, 0);
        createBall(4.5f, -10);





        for (int i= 0; i < bodies.size(); i++){
            System.out.println(bodies.get(i).getType());
        }

    }
    public void createGround(float xPixels, float yPixels, float widthPixels, float heightPixels) {
        // Convert pixel coordinates to meters for body creation
        float xMeters = pixelsToMeters(xPixels);
        float yMeters = pixelsToMeters(yPixels);
        float widthMeters = pixelsToMeters(widthPixels);
        float heightMeters = pixelsToMeters(heightPixels);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(xMeters, yMeters);

        Body groundBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(widthMeters / 2, heightMeters / 2); // Adjusted shape to use half-width and half-height
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.0f; // Set density to 0 for static objects
        fixtureDef.friction = 0.3f;

        groundBody.createFixture(fixtureDef);

        // Add the created body to the list
        bodies.add(groundBody);
    }

    private float pixelsToMeters(float xPixels) {
        float pixelsToMeters = 1.0f / 100;
        return xPixels * pixelsToMeters;
    }

    public void createBall(float x, float y) {
        float pixelsToMeters = 1.0f / 100;
        float pixelRadius = 150.0f;
        float meterRadius = pixelRadius * pixelsToMeters;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

        Body ballBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(meterRadius); // Adjusted the radius to match the scale
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        ballBody.setGravityScale(1.0f); // Adjusted the gravity scale

        ballBody.createFixture(fixtureDef);

        // Apply force if needed
        // ballBody.applyForce(new Vec2(0, 10.0f), ballBody.getWorldCenter());

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
