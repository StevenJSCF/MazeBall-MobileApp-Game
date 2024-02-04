package com.example.androidexample;

import org.java_websocket.handshake.ServerHandshake;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.URI;
import static org.junit.Assert.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ConstantWebSocketManagerTest {

    private ConstantWebSocketManager webSocketManager;

    @Before
    public void setUp() {
        webSocketManager = ConstantWebSocketManager.getInstance();
    }

    @After
    public void tearDown() {
        webSocketManager.disconnectAllWebSockets();
    }

    @Test
    public void testWebSocketConnection() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Define a WebSocket listener to track the open event
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onWebSocketOpen(ServerHandshake handshakedata) {
                latch.countDown();  // Decrement the latch when the WebSocket is open
            }

            @Override
            public void onWebSocketMessage(String message) {
                // Implement as needed
            }

            @Override
            public void onWebSocketClose(int code, String reason, boolean remote) {
                // Implement as needed
            }

            @Override
            public void onWebSocketError(Exception ex) {
                // Implement as needed
            }

            @Override
            public void onWebSocketConnectionSuccess() {

            }

            @Override
            public void onWebSocketConnectionFailure(Exception ex) {

            }
        };

        // Connect WebSocket
        webSocketManager.setWebSocketListener(webSocketListener);
        webSocketManager.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/mainMenu/4");

        // Wait for the latch to count down or timeout after 10 seconds
        assertTrue(latch.await(10, TimeUnit.SECONDS));

        // Assert that the WebSocket is open
        assertTrue(webSocketManager.isRunning());
    }

    @Test
    public void testSendMessage() {
        // Connect WebSocket
        webSocketManager.connectWebSocket("ws://example.com");

        // Send a message
        webSocketManager.sendMessage("Test message");

        // Assert that the message was sent successfully (no exceptions thrown)
        // Note: This test does not guarantee that the message was received on the other end
    }

    @Test
    public void testDisconnectAllWebSockets() {
        // Connect WebSocket
        webSocketManager.connectWebSocket("ws://example.com");

        // Disconnect all WebSockets
        webSocketManager.disconnectAllWebSockets();

        // Assert that there are no running WebSockets
        assertFalse(webSocketManager.isRunning());
    }
}
