package com.example.androidexample;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.androidexample.MyBackgroundService;
import com.example.androidexample.WebSocketListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MyBackgroundServiceTest {

    private MyBackgroundService myBackgroundService;

    @Before
    public void setUp() {
        myBackgroundService = new MyBackgroundService();
    }

    @After
    public void tearDown() {
        myBackgroundService = null;
    }

    @Test
    public void testInitPersistentWebSocket() {
        // Mock WebSocketManager
        ConstantWebSocketManager constantWebSocketManager = new ConstantWebSocketManager();
        myBackgroundService.persistentWebSocketManager = constantWebSocketManager;

        // Test initialization of persistent WebSocket
        myBackgroundService.initPersistentWebSocket();

        assertNotNull(myBackgroundService.persistentWebSocketManager);
        assertTrue(myBackgroundService.persistentWebSocketManager.isRunning());
    }

    @Test
    public void testConnectPersistentWebSocket() {
        // Mock WebSocketManager
        ConstantWebSocketManager constantWebSocketManager = new ConstantWebSocketManager();
        myBackgroundService.persistentWebSocketManager = constantWebSocketManager;

        // Test connecting to persistent WebSocket
        myBackgroundService.connectPersistentWebSocket();

        assertNotNull(myBackgroundService.persistentWebSocketManager);
        assertTrue(myBackgroundService.persistentWebSocketManager.isRunning());
    }

    @Test
    public void testSendNotification() {
        // Mock WebSocketManager
        ConstantWebSocketManager constantWebSocketManager = new ConstantWebSocketManager();
        myBackgroundService.persistentWebSocketManager = constantWebSocketManager;

        // Test sending notification
        myBackgroundService.sendNotification("user123", "recipient123", "gameMode123", "levelID123");

        assertNotNull(myBackgroundService.persistentWebSocketManager);
      //  assertEquals("user123,recipient123,gameMode123,levelID123", constantWebSocketManager.getSentMessage());
    }
}

