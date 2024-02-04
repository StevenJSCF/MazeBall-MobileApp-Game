package com.example.androidexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.java_websocket.handshake.ServerHandshake;

public class MyBackgroundService extends Service implements WebSocketListener {

    private static final String TAG = "MyBackgroundService";
    private static final String USER_ID_KEY = "USER_ID_KEY";

    static ConstantWebSocketManager persistentWebSocketManager;
    //private WebSocketManager levelEditorWebSocketManager;

    private volatile boolean isRunning = true;
    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();

        userId = MainActivity.getID();

        // Initialize the persistent WebSocket connection
        initPersistentWebSocket();

        // Connect to the persistent WebSocket
        connectPersistentWebSocket();

        // Initialize the WebSocket connection for LevelEditor screen

    }

    void initPersistentWebSocket() {
        try {
            // Set up the persistent WebSocket connection
            persistentWebSocketManager = ConstantWebSocketManager.getInstance();
            persistentWebSocketManager.setWebSocketListener(this);

            // Connect to the persistent WebSocket with the appropriate URL
            String persistentWebSocketUrl = "ws://chiang04@coms-309-021.class.las.iastate.edu:8080/mainMenu/" + userId;
            persistentWebSocketManager.connectWebSocket(persistentWebSocketUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void connectPersistentWebSocket() {
        if (!persistentWebSocketManager.isRunning()) {
            persistentWebSocketManager.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/mainMenu/" + userId);
        }
    }


    static void sendNotification(String userId, String recipient, String gamemode, String levelID) {
        String message = userId + "," + recipient + "," + gamemode + "," + levelID ;
        System.out.println(message);
        System.out.println("Before sending notification");
        System.out.println(recipient);
        if (persistentWebSocketManager != null) {
            persistentWebSocketManager.sendMessage(message);

            System.out.println("Sending notification");
        } else {
            System.out.println("WebSocketManager is null. Unable to send notification.");
        }
        System.out.println("After sending notification");
    }



    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MyBackgroundService getService() {
            return MyBackgroundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public static final String ACTION_WEBSOCKET_MESSAGE = "com.example.androidexample.WEBSOCKET_MESSAGE";
    public static final String EXTRA_WEBSOCKET_MESSAGE = "websocket_message";


    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d(TAG, "WebSocket Connected");
    }

    @Override
    public void onWebSocketMessage(String message) {
       // System.out.println("Received message: " + message);
        Log.d(TAG, "WebSocket Message received: " + message);
       // Log.d(TAG, "WebSocket Message received: " + message);

        // Broadcast the WebSocket message to any interested components
        Intent broadcastIntent = new Intent(ACTION_WEBSOCKET_MESSAGE);
        broadcastIntent.putExtra(EXTRA_WEBSOCKET_MESSAGE, message);
        sendBroadcast(broadcastIntent);
        // Process WebSocket message
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d(TAG, "WebSocket Closed");
        // Handle WebSocket close event and attempt to reconnect if needed
        if (isRunning && persistentWebSocketManager != null) {
           // persistentWebSocketManager.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/mainMenu/" + userId);
        }
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d(TAG, "WebSocket Error: " + ex.getMessage());
    }

    @Override
    public void onWebSocketConnectionSuccess() {

    }

    @Override
    public void onWebSocketConnectionFailure(Exception ex) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Disconnect WebSocket connections when the service is destroyed
        if (persistentWebSocketManager != null) {
        }

        isRunning = false;
        Log.d(TAG, "Service destroyed.");
    }
}
