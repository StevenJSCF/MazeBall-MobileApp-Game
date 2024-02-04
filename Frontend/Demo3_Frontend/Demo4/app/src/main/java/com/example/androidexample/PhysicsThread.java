package com.example.androidexample;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.util.List;


public class PhysicsThread extends Thread {
    private final SurfaceHolder surfaceHolder;


    private final float scaleFactor = 100.0f;  // 1 meter in physics world = 100 pixels in canvas world


    private final PhysicsSimulator physicsSimulator;
    private boolean running;

    public PhysicsThread(SurfaceHolder holder, PhysicsSimulator simulator) {
        surfaceHolder = holder;
        physicsSimulator = simulator;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();

            // Update the physics simulation
            physicsSimulator.update(0.016f);

            // Render the physics simulation
            render();

            long frameTime = System.currentTimeMillis() - startTime;

            // Sleep to maintain the target frame rate
            if (frameTime < 20) {
                try {
                    Thread.sleep(20 - frameTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void render() {
        Canvas canvas = surfaceHolder.lockCanvas();

        if (canvas != null) {
            try {
                // Clear canvas


                canvas.drawColor(Color.WHITE);

                Paint paint = new Paint();
                paint.setColor(Color.CYAN);



                List<Body> bodies = physicsSimulator.getBodies();
                //print all bodies

                try {
                    for (Body body : bodies) {
                        Fixture fixture = body.getFixtureList();
                        Shape shape = fixture.getShape();

                        if(body != null) {
                            if (shape.getType() == ShapeType.POLYGON) {
                                // Rendering code for polygons
                                renderPolygon(canvas, paint, body, (PolygonShape) shape);
                            } else if (shape.getType() == ShapeType.CIRCLE) {
                                if (body.isActive()) {
                                    // Rendering code for circles
                                    renderCircle(canvas, paint, body, (CircleShape) shape);
                                }
                            }
                        }
                    }
                    Ball[][] listOfBalls = physicsSimulator.getListOfBalls();

                    if (listOfBalls != null) {
                        for (int i = 0; i < listOfBalls.length; i++) {
                            for (int j = 0; j < listOfBalls[i].length; j++) {
                                Ball body = listOfBalls[i][j];

                                    // Check if the body is not null
                                    Fixture fixture = body.getFixtureList();
                                    if (fixture != null) { // Check if the fixture is not null
                                        Shape shape = fixture.getShape();
                                        if (shape != null) { // Check if the shape is not null
                                            if(listOfBalls[i][j].getNumber() !=0 ){
                                            if (listOfBalls[i][j].getNumber() == 1) {
                                                paint.setColor(Color.BLUE);
                                            } else if (listOfBalls[i][j].getNumber() == 2) {
                                                paint.setColor(Color.RED);
                                            } else if (listOfBalls[i][j].getNumber() == 3) {
                                                paint.setColor(Color.GREEN);
                                            }else if (listOfBalls[i][j].getNumber() == 4) {
                                                paint.setColor(Color.CYAN);
                                            }else if (listOfBalls[i][j].getNumber() == 6) {
                                                paint.setColor(Color.MAGENTA);
                                            }
                                            renderCircle(canvas, paint, body, (CircleShape) shape);

                                        }
                                    }
                                }
                            }
                        }

                    }
                    connectBalls(canvas, paint, physicsSimulator.getListOfBalls());
                    renderParticles(canvas, paint, physicsSimulator.getParticleSystem());



                } catch (Exception e) {
                    System.out.println("Error in rendering" + e);
                }

            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void connectBalls(Canvas canvas, Paint paint, Ball[][] balls) {
        int numRows = balls.length;
        int numCols = balls[0].length;

        // Iterate through the rows
        for (int row = 0; row < numRows; row++) {
            // Iterate through the columns
            for (int col = 0; col < numCols; col++) {


                // Get the current ball
                Ball currentBall = balls[row][col];


                int number = currentBall.getNumber();
                if(number == 0){
                    continue;
                }
                if (number == 1) {
                    paint.setColor(Color.BLUE);
                } else if (number == 2){
                    paint.setColor(Color.RED);
                } else if (number == 3){
                    paint.setColor(Color.GREEN);
                }else if (number == 4){
                    paint.setColor(Color.CYAN);
                }else if (number == 6){
                    paint.setColor(Color.MAGENTA);
                }


                // Check if there's a ball to the right
                if (row < numRows - 1) {
                    Ball bottomBall = balls[row + 1][col];
                    if (bottomBall.getNumber() != 0) {
                        connectTwoBalls(canvas, paint, currentBall, bottomBall);
                    }
                }

                if (col < numCols - 1) {
                    Ball rightBall = balls[row][col + 1];
                    if (rightBall.getNumber() != 0) {
                        connectTwoBalls(canvas, paint, currentBall, rightBall);
                    }
                }

                // Check if there's a ball below

            }
        }
    }

    private void connectTwoBalls(Canvas canvas, Paint paint, Ball ball1, Ball ball2) {
        if (ball1 != null && ball2 != null) {
            // Get the positions of the two balls in pixels
            float x1 = metersToPixels(ball1.getPosition().x, scaleFactor);
            float y1 = metersToPixels(ball1.getPosition().y, scaleFactor);
            float x2 = metersToPixels(ball2.getPosition().x, scaleFactor);
            float y2 = metersToPixels(ball2.getPosition().y, scaleFactor);

            paint.setStrokeWidth(25); // Adjust the value as needed

            // Draw a line connecting the two balls
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }




    private void renderPolygon(Canvas canvas, Paint paint, Body body, PolygonShape fixture) {
        PolygonShape polygon = fixture;
        Vec2[] vertices = polygon.getVertices();
        int vertexCount = polygon.getVertexCount();

        // Calculate the width and height of the rectangle
        float width = metersToPixels(vertices[2].x - vertices[0].x, scaleFactor);
        float height = metersToPixels(vertices[2].y - vertices[0].y, scaleFactor);

        // Calculate the top-left corner position of the rectangle based on the center
        float screenX = metersToPixels(body.getPosition().x, scaleFactor) - width / 2;
        float screenY = metersToPixels(body.getPosition().y, scaleFactor) - height / 2;

        // Draw the rectangle
        RectF rect = new RectF(screenX, screenY, screenX + width, screenY + height);
        canvas.drawRect(rect, paint);
    }



    private float metersToPixels(float meters, float scaleFactor) {
        return meters * scaleFactor;
    }



    private void renderCircle(Canvas canvas, Paint paint, Body body, CircleShape fixture) {
        CircleShape circle = fixture;
        float radius = circle.getRadius();

        // Get the position of the body in meters
        Vec2 position = body.getPosition();

        // Convert the meter position to pixel position for rendering
        float pixelX = position.x * scaleFactor;
        float pixelY = position.y * scaleFactor;


        // Draw the circle at the pixel position
        canvas.drawCircle(pixelX, pixelY, radius * 100, paint);
        // Log the position in meters (for debugging)
        //System.out.println(position.x + " " + position.y);
    }

    private void renderParticles(Canvas canvas, Paint paint, ParticleSystem particleSystem) {
        if(particleSystem == null){
            return;
        }

        List<Particle> particles = particleSystem.getParticles();

        for (Particle particle : particles) {
            Vec2 position = particle.getPosition();
            float radius = particle.getRadius();

            float pixelX = metersToPixels(position.x, scaleFactor);
            float pixelY = metersToPixels(position.y, scaleFactor);

            // Draw the particle as a circle at the pixel position
            canvas.drawCircle(pixelX, pixelY, radius * scaleFactor, paint);
        }
    }




    public void stopThread() {
        running = false;
    }
}
