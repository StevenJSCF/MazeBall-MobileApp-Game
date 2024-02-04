package com.example.androidexample;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ConstantWebSocketManager {

    private static ConstantWebSocketManager instance;
    private List<MyWebSocketClient> webSocketClients = new ArrayList<>();
    private WebSocketListener webSocketListener;
    private List<NotificationHandler> notificationHandlers = new ArrayList<>();
    private boolean isRunning = false;

    ConstantWebSocketManager() {}

    public static synchronized ConstantWebSocketManager getInstance() {
        if (instance == null) {
            instance = new ConstantWebSocketManager();
        }
        return instance;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setWebSocketListener(WebSocketListener listener) {
        this.webSocketListener = listener;
    }

    public void removeWebSocketListener() {
        this.webSocketListener = null;
    }

    public void addNotificationHandler(NotificationHandler handler) {
        notificationHandlers.add(handler);
    }

    public void connectWebSocket(String serverUrl) {
        try {
            URI serverUri = URI.create(serverUrl);
            MyWebSocketClient webSocketClient = new MyWebSocketClient(serverUri, webSocketListener, notificationHandlers);
            webSocketClients.add(webSocketClient);
            webSocketClient.connect();
            isRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        for (MyWebSocketClient webSocketClient : webSocketClients) {
            if (webSocketClient.isOpen()) {
                webSocketClient.send(message);
            }
        }
    }

    public void disconnectAllWebSockets() {
        for (MyWebSocketClient webSocketClient : webSocketClients) {
            webSocketClient.close();
        }
        webSocketClients.clear();
        isRunning = false;
    }

    private static class MyWebSocketClient extends WebSocketClient {

        private WebSocketListener webSocketListener;
        private List<NotificationHandler> notificationHandlers;

        private MyWebSocketClient(URI serverUri, WebSocketListener listener, List<NotificationHandler> handlers) {
            super(serverUri);
            this.webSocketListener = listener;
            this.notificationHandlers = handlers;
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            // Implement as needed
            if (webSocketListener != null) {
                webSocketListener.onWebSocketOpen(handshakedata);
            }
        }

        @Override
        public void onMessage(String message) {
            // Implement as needed
            if (webSocketListener != null) {
                webSocketListener.onWebSocketMessage(message);

                // Forward the message to all registered notification handlers
                for (NotificationHandler handler : notificationHandlers) {
                    handler.onNotificationReceived(message);
                }
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            // Implement as needed
            if (webSocketListener != null) {
                webSocketListener.onWebSocketClose(code, reason, remote);
            }
        }

        @Override
        public void onError(Exception ex) {
            // Implement as needed
            if (webSocketListener != null) {
                webSocketListener.onWebSocketError(ex);
            }
        }
    }
}
