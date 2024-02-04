
package com.example.as1;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class PhysicsSimulator {
    private World world;

    public PhysicsSimulator() {
        // Create a new physics world with gravity
        world = new World();
        world.setGravity(new Vector2(0, 100)); // Gravity in m/s^2
        createBall(100,100);
    }

    public void createBall(float x, float y) {
        // Create a ball body
        Circle ballShape = new Circle(100);
        org.dyn4j.dynamics.Body ballBody = new org.dyn4j.dynamics.Body();
        ballBody.addFixture(ballShape); // Use the same shape for the fixture
        ballBody.setMass(MassType.NORMAL);
        ballBody.translate(x, y); // Initial position

        // Add the ball to the world
        world.addBody(ballBody);
    }


    public void update(double elapsedTime) {
        // Update the physics simulation
        world.update(elapsedTime);
    }

    public World getWorld() {
        return world;
    }
}
