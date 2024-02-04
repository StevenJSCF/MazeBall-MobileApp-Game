package com.example.androidexample;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebSocketManager2Test {

    @Test
    public void testConnectWebSocket() {
        WebSocketManager2 webSocketManager2 = WebSocketManager2.getInstance();

        // Replace "wss://example.com" with your WebSocket server URL
        webSocketManager2.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/4/4");

        // Assuming MyWebSocketClient is accessible for testing, you might want to check if it's created and connected
       // assertNotNull(webSocketManager.getWebSocketClient());
        //assertTrue(webSocketManager.getWebSocketClient().isOpen());
    }

    @Test
    public void testSendMessage() {
        WebSocketManager2 webSocketManager2 = WebSocketManager2.getInstance();

        // Replace "wss://example.com" with your WebSocket server URL
        webSocketManager2.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/4/4");

        // Replace "Hello, WebSocket!" with the message you want to test
        webSocketManager2.sendMessage("Hello, WebSocket!");

        // Assuming MyWebSocketClient is accessible for testing, you might want to check if the message is sent
        // Note: Adjust the actual assertions based on your WebSocket implementation
        //assertEquals("Hello, WebSocket!", webSocketManager.getWebSocketClient().getLastSentMessage());
    }

    @Test
    public void testDisconnectWebSocket() {
        WebSocketManager2 webSocketManager2 = WebSocketManager2.getInstance();

        // Replace "wss://example.com" with your WebSocket server URL
        webSocketManager2.connectWebSocket("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/4/4");

        webSocketManager2.disconnectWebSocket();

        // Assuming MyWebSocketClient is accessible for testing, you might want to check if it's closed
        //assertFalse(webSocketManager.getWebSocketClient().isOpen());
    }

    @Test
    public void testWebSocketManagerSingleton() {
        WebSocketManager2 instance1 = WebSocketManager2.getInstance();
        WebSocketManager2 instance2 = WebSocketManager2.getInstance();

        // Ensure that the WebSocketManager is a singleton
        assertSame(instance1, instance2);
    }
}
