package com.example.as1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.util.List;

public class PhysicsThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private PhysicsSimulator physicsSimulator;
    private boolean running;

    public PhysicsThread(SurfaceHolder holder, PhysicsSimulator simulator) {
        surfaceHolder = holder;
        physicsSimulator = simulator;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            // Calculate elapsed time
            long startTime = System.currentTimeMillis();

            // Update the physics simulation
            physicsSimulator.update(0.016f);


            // Draw the physics simulation
            Canvas canvas = surfaceHolder.lockCanvas();

            if (canvas != null) {
                try {
                    // Clear canvas
                    canvas.drawColor(Color.WHITE);

                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);

                    // Draw physics bodies here (e.g., circles)
                    List<Body> bodies = physicsSimulator.getBodies();
                    for (Body body : bodies) {
                        int fixtureCount = bodies.size();
                        for (int i = 0; i < fixtureCount; i++) {
                            Fixture fixture = body.getFixtureList();
                            org.jbox2d.collision.shapes.Shape shape = fixture.getShape();
                            if (shape.getType() == org.jbox2d.collision.shapes.ShapeType.CIRCLE) {
                                org.jbox2d.collision.shapes.CircleShape circle = (org.jbox2d.collision.shapes.CircleShape) shape;


                                    // PositionX, Y are sprite's positions
                                float PositionX = body.getPosition().x; //* 32;
                                float PositionY = body.getPosition().y* 32;

                                Vec2 position = body.getPosition();
                                float radius = circle.m_radius;


                                // Map physics coordinates to screen coordinates if needed
                                // Adjust as needed based on your screen and physics world dimensions
                                // Example: x = x * pixelsPerMeter, y = screenHeight - y * pixelsPerMeter

                                // Draw the circle on the canvas
                                canvas.drawCircle(PositionX, PositionY, radius, paint);
                                System.out.printf("x: %f, y: %f, radius: %f\n", PositionX, PositionX, radius);
                            }
                        }
                    }
                } finally {
                    // Make sure to unlock the canvas when done
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            // Calculate time taken for this frame
            long frameTime = System.currentTimeMillis() - startTime;

            // Sleep to maintain target frame rate
            if (frameTime < 20) {
                try {
                    Thread.sleep(20 - frameTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopThread() {
        running = false;
    }
}
