package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class MainMenu extends AppCompatActivity implements NotificationHandler {

    Context Context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        IntentFilter filter = new IntentFilter(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE);
        registerReceiver(websocketMessageReceiver, filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add a back button to the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button btnStartLevel = findViewById(R.id.btnStartLevel);
        Button btnEditLevel = findViewById(R.id.btnEditLevel);
        Button btnLoadLevel = findViewById(R.id.btnLoadLevel);
        Button btnUserActivity = findViewById(R.id.btnUserActivity);
        Button buttonToLeaderboard = findViewById(R.id.btnLeaderboard);

        buttonToLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Store activity
                Animation animation = AnimationUtils.loadAnimation(Context, R.anim.button_scale);
                v.startAnimation(animation);
                Intent intent = new Intent(MainMenu.this, LeaderBoard.class);
                startActivity(intent);
            }
        });



        btnStartLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Context, R.anim.button_scale);
                view.startAnimation(animation);
                Intent intent = new Intent(MainMenu.this, LevelList.class);
                startActivity(intent);
            }
        });

        btnUserActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, MainScreenActivity.class);
                intent.putExtra("userUrl", "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/" + MainActivity.getID());
                startActivity(intent);
            }
        });

        btnEditLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Context, R.anim.button_scale);
                view.startAnimation(animation);
                Intent intent = new Intent(MainMenu.this, LevelEditor.class);
                startActivity(intent);
            }
        });

        btnLoadLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Context, R.anim.button_scale);
                view.startAnimation(animation);
                // Define an intent to start the "Load Level" activity
                Intent intent = new Intent(MainMenu.this, LevelScreen.class);

                String worldData = "your_world_data_here";
                intent.putExtra("world-data", worldData);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onNotificationReceived(String notificationMessage) {
        // Handle notification message received from WebSocket
        Log.d("MainMenu", "Notification received: " + notificationMessage);

        // Build and display a notification
        NotificationHelper.showNotification(this, "New Notification", notificationMessage);
    }
    private BroadcastReceiver websocketMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE)) {
                String message = intent.getStringExtra(MyBackgroundService.EXTRA_WEBSOCKET_MESSAGE);
                // Display the message on your UI
                System.out.println("Received message: " + message);
                Drawnotification.showGameInvitationDialog(context, message);


            }
        }
    };
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


