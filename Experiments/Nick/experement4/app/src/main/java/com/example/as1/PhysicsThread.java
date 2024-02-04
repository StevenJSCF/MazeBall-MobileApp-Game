

package com.example.as1;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import org.dyn4j.dynamics.Body;

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
            physicsSimulator.update(0.016);

            // Draw the physics simulation
            Canvas canvas = surfaceHolder.lockCanvas();
            System.out.println("canvas: " + canvas);

            if (canvas != null) {
                try {

                    // Clear canvas
                    canvas.drawColor(Color.WHITE);

                    // Define the square's position (left, top, right, bottom) and color
                    int left = 100;          // X-coordinate of the left edge
                    int top = 100;           // Y-coordinate of the top edge
                    int size = 200;          // Size of the square (width and height)
                    int right = left + size; // Calculate the right edge
                    int bottom = top + size; // Calculate the bottom edge
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);

                    // Draw the square on the canvas
                    canvas.drawRect(left, top, right, bottom, paint);



                    for (org.dyn4j.dynamics.Body body : physicsSimulator.getWorld().getBodies()) {

                        if (body.getFixtureCount() > 0) {

                            org.dyn4j.geometry.Circle circle = (org.dyn4j.geometry.Circle) body.getFixture(0).getShape();
                            float radius = (float) circle.getRadius();
                            float x = (float) body.getTransform().getTranslationX();
                            float y = (float) body.getTransform().getTranslationY();

                            // Draw the circle on the canvas
                            canvas.drawCircle(x, y, radius, paint);
                            System.out.printf( "x: %f, y: %f, radius: %f\n", x, y, radius);

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
            if (frameTime < 16) {
                try {
                    Thread.sleep(16 - frameTime);
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
