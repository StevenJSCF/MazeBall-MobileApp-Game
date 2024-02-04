package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class MainActivity extends AppCompatActivity {



    private World world;
    private static final float GRAVITY = -10.0f; // Adjust as needed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        world = new World(new Vec2(0, GRAVITY));
        createFallingBlock();

    }

    private void createFallingBlock() {
        BodyDef blockBodyDef = new BodyDef();
        blockBodyDef.type = BodyType.DYNAMIC; // Dynamic bodies are affected by forces.
        blockBodyDef.position.set(0, 10); // Set the initial position of the block.

        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox(1, 1); // Set the dimensions of the block.

        FixtureDef blockFixtureDef = new FixtureDef();
        blockFixtureDef.shape = blockShape;
        blockFixtureDef.density = 1.0f; // Adjust density as needed.
        blockFixtureDef.friction = 0.3f; // Adjust friction as needed.

        Body blockBody = world.createBody(blockBodyDef);
        blockBody.createFixture(blockFixtureDef);
    }

    private boolean isRunning = true;
    private float timeStep = 1.0f / 60.0f; // Time step (adjust as needed)
    private int velocityIterations = 6;
    private int positionIterations = 2;

    private void gameLoop() {
        while (isRunning) {
            // Update the JBox2D world
            world.step(timeStep, velocityIterations, positionIterations);

            // Render the objects
            // Implement your rendering logic here

            // Sleep to control the frame rate (optional)
            try {
                Thread.sleep(16); // Adjust for desired frame rate (e.g., 60 FPS)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private Thread gameThread;

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}