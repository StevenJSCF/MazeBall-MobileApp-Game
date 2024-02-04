package com.example.as1;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtils {
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (windowManager != null) {
            // Get the default display
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            // Retrieve the screen height in pixels
            return displayMetrics.heightPixels;
        }

        // Return a default height (or handle the error accordingly)
        return 0;
    }
}
