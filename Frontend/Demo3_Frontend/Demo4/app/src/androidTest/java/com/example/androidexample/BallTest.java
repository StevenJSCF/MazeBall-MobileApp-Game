package com.example.androidexample;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BallTest {

    private World world;
    private Ball ball;

    @Before
    public void setUp() {
        world = new World( new Vec2(0, 0));
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        ball = new Ball(bodyDef, world, 1, BodyType.DYNAMIC);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, ball.getNumber());
        assertEquals(BodyType.DYNAMIC, ball.getBodyType());
        assertFalse(ball.isDyanmic());
        assertFalse(ball.isDynamicSet());
        assertFalse(ball.isGravity());
        assertFalse(ball.isGravitySet());
    }

    @Test
    public void testSetNumber() {
        ball.setNumber(5);
        assertEquals(5, ball.getNumber());
        assertEquals(5, ball.tempnumber);
    }

    @Test
    public void testUpdateBalls() {
        ball.setNumber(0);
        ball.updateBalls();
        assertFalse(ball.isActive());

        ball.setNumber(4);
        ball.updateBalls();
        assertFalse(ball.isActive());

        ball.setNumber(3);
        ball.updateBalls();
        assertTrue(ball.isActive());
    }

    @Test
    public void testSetActive() {
        ball.SetActive(false);
        assertFalse(ball.isActive());

        ball.SetActive(true);
        assertTrue(ball.isActive());
    }

    @Test
    public void testGetCounter() {
        assertEquals(0, ball.getCounter());
    }

    // Add more test cases for other methods as needed

}
