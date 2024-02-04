package com.example.as1;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Transform;
import org.jbox2d.dynamics.Body;
import org.jbox2d.collision.shapes.PolygonShape;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import java.util.ArrayList;
import java.util.List;

public class DynamicBox {
    private Body body;
    private PolygonShape shape;

    public DynamicBox(Body body, PolygonShape shape) {
        this.body = body;
        this.shape = shape;
    }

    public Body getBody() {
        return body;
    }

    public PolygonShape getShape() {
        return shape;
    }

    // Method to update the position of the dynamic box
    public void updatePosition(float x, float y) {
        body.setTransform(new Vec2(x, -y), body.getAngle());
    }

    // Method to modify the shape of the dynamic box
    // Method to carve out a part of the shape based on touch input
    public void carveShape( Vec2 touchPoint, float radius) {
        touchPoint = new Vec2(pixelsToMeters(touchPoint.x), pixelsToMeters(touchPoint.y)); // Convert screen coordinates to meters
        // Create a circle shape definition
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        // Create a fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // Check if the touch point is inside the box
        if (body.getFixtureList().testPoint(touchPoint)) {
            // Remove the fixture from the box's body
            body.destroyFixture(body.getFixtureList());
        }
    }
    private float pixelsToMeters(float Pixels) {
        float pixelsToMeters = 1.0f / 100;
        return Pixels * pixelsToMeters;
    }
}
