package com.example.androidexample;

import android.annotation.SuppressLint;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import  java.util.ArrayList;
import java.util.List;

public class Ball extends Body implements ContactListener {
    private int number;
    private int counter;
    private World world;
    private List<Body> bodiesToDeactivate = new ArrayList<>();
    private List<Body> bodiesToChangeGravity = new ArrayList<>();
    int tempnumber;
    private float x;
    private float y ;
    private BodyType bodyType;
    private boolean isDyanmic = false;
    boolean isDynamicSet = false;
    private boolean isGravity = false;
    boolean isGravitySet = false;



    public Ball(BodyDef bd, World world, int number, BodyType bodyType) {

        super(bd, world);
        this.world = world;
        this.number = number;
        this.bodyType = bodyType;



        tempnumber = number;


        x = bd.position.x;
        y = bd.position.y;

        // Create and configure the fixture in the constructor.

            float pixelsToMeters = 1.0f / 100;
            float pixelRadius = 20.0f;
            float meterRadius = pixelRadius * pixelsToMeters;

            FixtureDef fixtureDef = new FixtureDef();
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(meterRadius);
            fixtureDef.shape = circleShape;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;
            createFixture(fixtureDef);


        // Initialize the collision counter to zero
        counter = 0;

        // Set this class as the contact listener
        world.setContactListener(this);
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
        tempnumber = number;
    }
    public void updateBalls() {
        number = tempnumber;
        if (number == 0 || number == 4){
            SetActive(false);
        } else{
            SetActive(true);
        }
        if (number == 4 && !isDynamicSet) {
            isDyanmic = true;
            isDynamicSet = true; // Set the flag to true to prevent further changes
        }



    }
    public void SetActive(boolean active) {
            try {
                //world.step(0, 0, 0);
                super.setActive(active);
            } catch (Exception e) {
                System.out.println("Error");
            }

    }
    public int getCounter() {
        return counter;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check if either of the fixtures is attached to a Ball with number 3
        if (fixtureA.getBody() instanceof Ball && ((Ball) fixtureA.getBody()).getNumber() == 3) {
            // Add fixtureB.getBody() to the list of bodies to deactivate
            bodiesToDeactivate.add(fixtureB.getBody());
            counter++;
        } else if (fixtureB.getBody() instanceof Ball && ((Ball) fixtureB.getBody()).getNumber() == 3) {
            // Add fixtureA.getBody() to the list of bodies to deactivate
            bodiesToDeactivate.add(fixtureA.getBody());
            counter++;
        }
        // Check if either of the fixtures is attached to a Ball with number 3
        if (fixtureA.getBody() instanceof Ball && ((Ball) fixtureA.getBody()).getNumber() == 6) {
            // add to a list of bodies to change the gravity
            System.out.println("Gravity");
            bodiesToChangeGravity.add(fixtureB.getBody());

        } else if (fixtureB.getBody() instanceof Ball && ((Ball) fixtureB.getBody()).getNumber() == 6) {
            System.out.println("Gravity");
            bodiesToChangeGravity.add(fixtureA.getBody());

        }
    }

    // Add a method to deactivate bodies after the time step
    public void deactivateBodies() {
        for (Body body : bodiesToDeactivate) {
            body.setActive(false);
        }
        bodiesToDeactivate.clear(); // Clear the list
    }
public void addbodiesToDeactivate(Body body) {
    bodiesToDeactivate.add(body);
}
public Body getBody(){
        return this;
}
    public void changeGravity() {
        for (Body body : bodiesToChangeGravity) {
            if(body.getGravityScale() == -9.8f) {
                body.setGravityScale(9.8f);
            }
            else {
                body.setGravityScale(-9.8f);
            }
        }
        bodiesToChangeGravity.clear(); // Clear the list
    }

    @Override
    public void endContact(Contact contact) {
        // Implement if needed
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Implement if needed
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Implement if needed
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public BodyType getBodyType() {
        return bodyType;
    }
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isDyanmic() {
        return isDyanmic;
    }
    public void setDyanmic(boolean dyanmic) {
        isDyanmic = dyanmic;
    }
    public boolean isDynamicSet() {
        return isDynamicSet;
    }
    public boolean isGravity() {
        return isGravity;
    }
    public void setGravity(boolean gravity) {
        isGravity = gravity;
    }
    public boolean isGravitySet() {
        return isGravitySet;
    }
    public void setGravitySet(boolean dynamicSet) {
        isGravitySet = dynamicSet;
    }

    public void setDynamicSet(boolean dynamicSet) {
        isDynamicSet = dynamicSet;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
