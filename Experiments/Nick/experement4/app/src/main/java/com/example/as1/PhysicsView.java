
package com.example.as1;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PhysicsView extends View {
    private Paint paint;
    private int squareX, squareY;
    private int squareSize = 100; // Size of the square
    private int squareSpeed = 5; // Speed of animation

    public PhysicsView(Context context) {
        super(context);
        init();
    }

    public PhysicsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Update square position
        squareX += squareSpeed;

        // If the square goes off-screen, reset its position
        if (squareX > getWidth() + squareSize) {
            squareX = -squareSize;
        }

        // Draw the square
        canvas.drawRect(squareX, squareY, squareX + squareSize, squareY + squareSize, paint);

        // Invalidate the view to trigger a redraw
        invalidate();
    }
}
