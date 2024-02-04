package com.example.as1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.SurfaceHolder;

import org.dyn4j.geometry.Vector2;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

import java.util.List;

public class PhysicsThread extends Thread {
    private SurfaceHolder surfaceHolder;


    private float scaleFactor = 100.0f;  // 1 meter in physics world = 100 pixels in canvas world


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
                paint.setColor(Color.BLUE);

                List<Body> bodies = physicsSimulator.getBodies();

                for (Body body : bodies) {
                    Fixture fixture = body.getFixtureList();
                    Shape shape = fixture.getShape();

                    if (shape.getType() == ShapeType.POLYGON) {
                        // Rendering code for polygons
                        renderPolygon(canvas, paint, body);
                    } else if (shape.getType() == ShapeType.CIRCLE) {
                        // Rendering code for circles
                        renderCircle(canvas, paint, body);
                    }
                }
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void renderPolygon(Canvas canvas, Paint paint, Body body) {
        PolygonShape polygon = (PolygonShape) body.getFixtureList().getShape();
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



    private void renderCircle(Canvas canvas, Paint paint, Body body) {
        CircleShape circle = (CircleShape) body.getFixtureList().getShape();
        float radius = circle.getRadius();

        // Get the position of the body in meters
        Vec2 position = body.getPosition();

        // Convert the meter position to pixel position for rendering
        float pixelX = position.x * 100; // X is your scale factor (e.g., 100 pixels = 1 meter)
        float pixelY = position.y * 100;

        // Draw the circle at the pixel position
        canvas.drawCircle(pixelX, pixelY, radius * 100, paint);

        // Log the position in meters (for debugging)
        System.out.println(position.x + " " + position.y);
    }





    public void stopThread() {
        running = false;
    }
}
