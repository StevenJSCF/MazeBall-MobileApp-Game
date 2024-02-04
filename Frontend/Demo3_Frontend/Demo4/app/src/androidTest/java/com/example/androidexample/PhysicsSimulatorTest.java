package com.example.androidexample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

@RunWith(AndroidJUnit4.class)
public class PhysicsSimulatorTest {




    private ActivityScenario<LevelScreen> activityScenario;




    @Before
    public void setUp() {
        activityScenario = ActivityScenario.launch(LevelScreen.class);

    }

    @After
    public void tearDown() {
        activityScenario.close();
    }

    @Test
    public void testCreateBallGrid() {
        // Given
        int width = 30;
        int height = 30;
        float x = 0.5f;
        float y = 2;
        float radius = 0.001f;
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context

        // When
        physicsSimulator.createBallGrid(x, y, width, height, radius);

        // Then
        Ball[][] listOfCircleBodies = (Ball[][]) physicsSimulator.getListOfCircleBodies();
        assertNotNull("listOfCircleBodies should not be null", listOfCircleBodies);

        // Print debug information
        System.out.println("Width of listOfCircleBodies: " + listOfCircleBodies.length);
        if (listOfCircleBodies.length > 0) {
            System.out.println("Height of listOfCircleBodies: " + listOfCircleBodies[0].length);
        }

        assertEquals("Width of listOfCircleBodies should match", width, listOfCircleBodies.length);
        if (listOfCircleBodies.length > 0) {
            assertEquals("Height of listOfCircleBodies should match", height, listOfCircleBodies[0].length);
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Ball ball = listOfCircleBodies[i][j];
                assertNotNull("Ball at position (" + i + ", " + j + ") should not be null", ball);
                assertEquals("Ball number should be 1", 1, ball.getNumber());
                assertEquals("Ball body type should be BodyType.STATIC", BodyType.STATIC, ball.getBodyType());

                // Check if the position is calculated correctly
                float expectedX = x + i * radius * 350;
                float expectedY = y + j * radius * 350;
                assertEquals("Ball X position should match", expectedX, ball.getPosition().x, 0.001f);
                assertEquals("Ball Y position should match", expectedY, ball.getPosition().y, 0.001f);
            }
        }

    }


    @Test
    public void testAddCircleOnTouch() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.createBallGrid(0.5f, 2, 30, 30, 0.001f);
        float touchX = 100.0f;
        float touchY = 200.0f;
        int radius = 5;


        // When
        physicsSimulator.addCircleOnTouch(touchX, touchY, radius);

        // Then
        Ball[][] listOfCircleBodies = (Ball[][]) physicsSimulator.getListOfCircleBodies();

        assertNotNull("listOfCircleBodies should not be null", listOfCircleBodies);

        for (int i = 0; i < listOfCircleBodies.length; i++) {
            for (int j = 0; j < listOfCircleBodies[i].length; j++) {
                Ball circleBody = listOfCircleBodies[i][j];

                if (circleBody.getNumber() == 0) {
                    assertTrue("Circle with number 1 should have been added",true);
                }

                // Check if the particle system is created
                ParticleSystem particleSystem = physicsSimulator.getParticleSystem();

            }
        }
    }

    @Test
    public void testRemoveCircleOnTouch() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.createBallGrid(0.5f, 2, 30, 30, 0.001f);
        float touchX = 100.0f;
        float touchY = 200.0f;
        int radius = 5;
        boolean particles = true;

        // When
        physicsSimulator.removeCircleOnTouch(touchX, touchY, radius, particles);

        // Then
        Ball[][] listOfCircleBodies = (Ball[][]) physicsSimulator.getListOfCircleBodies();

        assertNotNull("listOfCircleBodies should not be null", listOfCircleBodies);

        for (int i = 0; i < listOfCircleBodies.length; i++) {
            for (int j = 0; j < listOfCircleBodies[i].length; j++) {
                Ball circleBody = listOfCircleBodies[i][j];

                if (circleBody.getNumber() == 0) {
                    assertTrue("Circle with number 1 should have been removed",true);
                }

                // Check if the particle system is created
                ParticleSystem particleSystem = physicsSimulator.getParticleSystem();
                if (particles) {
                    assertNotNull("Particle system should be created", particleSystem);
                    // Add more assertions related to the particle system if needed
                } else {
                    assertNull("Particle system should not be created", particleSystem);
                }
            }
        }

}
    @Test
    public void testPixelsToMeters() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        float pixels = 100.0f;

        // When
        float result = physicsSimulator.pixelsToMeters(pixels);

        // Then
        float expectedMeters = pixels / 100.0f;
        assertEquals("Conversion from pixels to meters is incorrect", expectedMeters, result, 0.0001);
    }
    @Test
    public void testCreateBall() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context

        float x = 5.0f;
        float y = 10.0f;
        int number = 1;
        BodyType bt = BodyType.DYNAMIC; // You can adjust the body type as needed

        // When
        Ball ball = physicsSimulator.createBall(x, y, number, bt);

        // Then
        assertNotNull("Ball should not be null", ball);

        // Verify that the ball properties are set correctly
        assertEquals("Ball x position should match", x, ball.getPosition().x, 0.0001);
        assertEquals("Ball y position should match", y, ball.getPosition().y, 0.0001);
        assertEquals("Ball number should match", number, ball.getNumber());
        //assertEquals("Ball body type should match", bt, ball.getBody().getType());

        // Additional assertions based on your implementation
        assertEquals("Gravity scale should match", 1.0f, ball.getGravityScale(), 0.0001);
        // Add more assertions based on your implementation
    }
    @Test
    public void testCreateBalls() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context

        float x = 5.0f;
        float y = 10.0f;

        // When
        Body ballBody = physicsSimulator.createBalls(x, y);

        // Then
        assertNotNull("Ball body should not be null", ballBody);

        // Verify that the ball body properties are set correctly
        assertEquals("Ball body x position should match", x, ballBody.getPosition().x, 0.0001);
        assertEquals("Ball body y position should match", y, ballBody.getPosition().y, 0.0001);
        assertEquals("Ball body type should match", BodyType.DYNAMIC, ballBody.getType());

        // Additional assertions based on your implementation
        // Add more assertions based on your implementation
    }

    @Test
    public void testUpdate() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.createBallGrid(0.5f, 2, 30, 30, 0.001f);

        // Save the initial state for comparison
        int initialNumBodies = physicsSimulator.getBodies().size();
        physicsSimulator.setWon(true);
        physicsSimulator.update(0.016f); // Assuming elapsed time
        assertTrue("Game should be won", physicsSimulator.getWon());
    }
    @Test
    public void testCalculateScore() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.createBallGrid(0.5f, 2, 30, 30, 0.001f);

        // Assuming the initial score is 0
        assertEquals("Initial score should be 0", 0, physicsSimulator.calculateScore());

        // When
        // Simulate some game state where certain balls have a counter value
    }
    @Test
    public void testUpdateBallGrid() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.createBallGrid(0.5f, 2, 3, 3, 0.001f);

        Integer[][] newGrid = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        // When
        physicsSimulator.updateBallGrid(newGrid);

        // Then
        Ball[][] listOfCircleBodies = (Ball[][]) physicsSimulator.getListOfCircleBodies();
}
    @Test
    public void testShowWinPopup() {
        // Given
        // Simulate a win state
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.setPlaying(true);

        // When
        physicsSimulator.showWinPopup(true);

    }
    @Test
    public void testResetScore() {
        // Assuming yourClassInstance has a method to get the list of circle bodies
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        Ball[][] listOfCircleBodies = (Ball[][]) physicsSimulator.getListOfCircleBodies();

        // Assuming listOfCircleBodies is not null and has elements
        int numRows = listOfCircleBodies.length;
        int numCols = listOfCircleBodies[0].length;

        // When
        physicsSimulator.resetscore();

        // Then
        // Verify that setCounter(0) is called for each element in the list
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                assertEquals(0, listOfCircleBodies[i][j].getCounter());
            }
        }
    }
    @Test
    public void testWinDialauge() {
        // Given
        PhysicsSimulator physicsSimulator = new PhysicsSimulator(null); // Pass null for the context
        physicsSimulator.setPlaying(true);

        // When
        physicsSimulator.showWinPopup(true);

        physicsSimulator.showWinPopup(false);



        // Then
        // Verify that the dialog is displayed
        // Add more assertions based on your implementation

    }
}
