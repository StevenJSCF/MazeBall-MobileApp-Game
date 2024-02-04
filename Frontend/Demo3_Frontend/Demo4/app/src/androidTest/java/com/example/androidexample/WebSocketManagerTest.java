package com.example.androidexample;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class WebSocketManagerTest {

    @Test
    public void testConnectWebSocket() {
        WebSocketManager webSocketManager = WebSocketManager.getInstance();

        // Replace "wss://example.com" with your WebSocket server URL
        webSocketManager.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/4/4");

        // Assuming MyWebSocketClient is accessible for testing, you might want to check if it's created and connected
       // assertNotNull(webSocketManager.getWebSocketClient());
        //assertTrue(webSocketManager.getWebSocketClient().isOpen());
    }

    @Test
    public void testSendMessage() {
        WebSocketManager webSocketManager = WebSocketManager.getInstance();

        // Replace "wss://example.com" with your WebSocket server URL
        webSocketManager.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/4/4");

        // Replace "Hello, WebSocket!" with the message you want to test
        webSocketManager.sendMessage("Hello, WebSocket!");

        // Assuming MyWebSocketClient is accessible for testing, you might want to check if the message is sent
        // Note: Adjust the actual assertions based on your WebSocket implementation
        //assertEquals("Hello, WebSocket!", webSocketManager.getWebSocketClient().getLastSentMessage());
    }

    @Test
    public void testDisconnectWebSocket() {
        WebSocketManager webSocketManager = WebSocketManager.getInstance();

        // Replace "wss://example.com" with your WebSocket server URL
        webSocketManager.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/4/4");

        webSocketManager.disconnectWebSocket();

        // Assuming MyWebSocketClient is accessible for testing, you might want to check if it's closed
        //assertFalse(webSocketManager.getWebSocketClient().isOpen());
    }

    @Test
    public void testWebSocketManagerSingleton() {
        WebSocketManager instance1 = WebSocketManager.getInstance();
        WebSocketManager instance2 = WebSocketManager.getInstance();

        // Ensure that the WebSocketManager is a singleton
        assertSame(instance1, instance2);
    }
}
