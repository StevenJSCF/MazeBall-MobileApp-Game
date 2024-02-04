
package com.example.as1;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private PhysicsSimulator physicsSimulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        physicsSimulator = new PhysicsSimulator();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start a thread to update the physics simulation and draw objects
        PhysicsThread physicsThread = new PhysicsThread(surfaceView.getHolder(), physicsSimulator);
        physicsThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Not needed for this example
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Cleanup resources if needed
    }
}
