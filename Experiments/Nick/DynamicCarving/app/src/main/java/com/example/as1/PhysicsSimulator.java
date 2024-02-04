package com.example.as1;

import android.view.MotionEvent;

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
   private Body[][] listOfCircleBodies;

    public PhysicsSimulator() {
        // Create a new physics world with gravity
        world = new World(new Vec2(0f, 10)); // Gravity in m/s^2, adjusted for the direction
        bodies = new ArrayList<>();
      //  dynamicBoxes = new ArrayList<>();

        //createDynamicBox(450, 1000);

       createGround(450, 1500, 800, 200);
       listOfCircleBodies = new Body[30][30];
       createBallGrid(0.5f, 2, 30, 30, 0.001f);

        createBalls(5, 0);
        createBalls(4.5f, -10);
        //createBalls(4.5f, -10);
        //createBalls(4.5f, -10);





        for (int i= 0; i < bodies.size(); i++){
            System.out.println(bodies.get(i).getType());
        }

    }
public void createBallGrid(float x, float y, int width, int height, float radius){
    for (int i = 0; i < width; i++){
        for (int j = 0; j < height; j++){
            System.out.println("i: " + i + " j: " + j);
            Body ball =  createBall(x + i * radius * 350, y + j * radius * 350);
            listOfCircleBodies[i][j] = ball;

        }
    }
}

    public void removeCircleOnTouch(float touchX, float touchY) {
        try {
            // Convert touch coordinates to world coordinates (if needed)
            Vec2 touchPoint = new Vec2(pixelsToMeters(touchX), pixelsToMeters(touchY)-2);

            System.out.println("Touch point: " + touchPoint);

            // Check if listOfCircleBodies is not null and contains elements
            if ( listOfCircleBodies.length != 0) {
                // Iterate through the list of circle bodies


                for (int i = 0; i <  listOfCircleBodies.length; i++) {
                    for(int j = 0; j< listOfCircleBodies[i].length ; j++) {
                        if (listOfCircleBodies[i][j] != null) {
                            //System.out.println("i: " + i + " j: " + j);
                            Body circleBody = listOfCircleBodies[i][j];
                            // Get the shape of the circle body (assuming it is a circle
                            CircleShape circleShape = (CircleShape) circleBody.getFixtureList().getShape();

                            // Calculate the world coordinates of the circle's center
                            Vec2 circleCenter = circleBody.getWorldPoint(circleShape.m_p);

                            //   System.out.println("Circle center: " + circleCenter);

                            // Calculate the distance between the touch point and the circle's center
                            float distance = touchPoint.sub(circleCenter).length();

                            // System.out.println("Distance: " + distance);

                            // Check if the touch point is within the circle's radius
                            if (distance < circleShape.m_radius * 2) {
                                // Remove the circle from the world
                                try {
                                    System.out.println(circleBody.getPosition());
                                    //updateRemoved(1/60f, circleBody);
                                    world.step(0,0,0);
                                    circleBody.setActive(false);
                                    listOfCircleBodies[i][j] = null;
                                    bodies.remove(circleBody);
                                    System.out.println("Removed circle");

                                } catch (Exception e) {
                                    System.err.println("Error in removeCircleOnTouch: "); //+ e.getMessage());
                                    e.printStackTrace();
                                }
                                // Remove the circle body from the list (optional)


                                // Optionally, you can remove the break statement if you want to remove multiple circles in one touch.
                                // break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("listOfCircleBodies is null or empty");
            }
        } catch (Exception e) {
            System.err.println("Error in removeCircleOnTouch: " + e.getMessage());
            e.printStackTrace();
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

    private float pixelsToMeters(float Pixels) {
        float pixelsToMeters = 1.0f / 100;
        return Pixels * pixelsToMeters;
    }

    public Body createBall(float x, float y) {
        float pixelsToMeters = 1.0f / 100;
        float pixelRadius = 20.0f;
        float meterRadius = pixelRadius * pixelsToMeters;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
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

        return ballBody;
    }
    public Body createBalls(float x, float y) {
        float pixelsToMeters = 1.0f / 100;
        float pixelRadius = 25.0f;
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

        return ballBody;
    }

    public void update(float elapsedTime) {
        // Update the physics simulation
        world.step(elapsedTime, 6, 2); // Perform a time step (adjust parameters as needed)

    }
    public void updateRemoved(float elapsedTime,Body body) {
        // Update the physics simulation
        world.step(elapsedTime, 6, 2); // Perform a time step (adjust parameters as needed)
        world.destroyBody(body);

    }


    public Body[][] getListOfCircleBodies() {
        return listOfCircleBodies;
    }

    public List<Body> getBodies() {
        return bodies;
    }
}
