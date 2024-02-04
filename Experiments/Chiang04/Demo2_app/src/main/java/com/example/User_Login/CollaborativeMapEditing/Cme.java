package com.example.User_Login.CollaborativeMapEditing;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


//chat/{username}
//@Component
//@ServerEndpoint("/chat/{username}")
public class Cme {
//
//    // Store all socket session and their corresponding username
//    // Two maps for the ease of retrieval by key
//    private static Map <Session, String > sessionUsernameMap = new Hashtable < > ();
//    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();
//
//    // server side logger
//    private final Logger logger = LoggerFactory.getLogger(Cme.class);
//
//    /**
//     * This method is called when a new WebSocket connection is established.
//     *
//     * @param session represents the WebSocket session for the connected user.
//     * @param username username specified in path parameter.
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
//
//        // server side log
//        logger.info("[onOpen] " + username);
//
//        // Handle the case of a duplicate username
//        if (usernameSessionMap.containsKey(username)) {
//            session.getBasicRemote().sendText("Username already exists");
//            session.close();
//        }
//        else {
//            // map current session with username
//            sessionUsernameMap.put(session, username);
//
//            // map current username with session
//            usernameSessionMap.put(username, session);
//
//            // send to the user joining in
//            sendMessageToPArticularUser(username, "Welcome to the chat server, "+username);
//
//            // send to everyone in the chat
//            broadcast("User: " + username + " has Joined the Chat");
//        }
//    }
//
//    /**
//     * Handles incoming WebSocket messages from a client.
//     *
//     * @param session The WebSocket session representing the client's connection.
//     * @param message The message received from the client.
//     */
//    @OnMessage
//    public void onMessage(Session session, String message) throws IOException {
//
//        // get the username by session
//        String username = sessionUsernameMap.get(session);
//
//        // server side log
//        logger.info("[onMessage] " + username + ": " + message);
//
//        // Direct message to a user using the format "@username <message>"
//        if (message.startsWith("@")) {
//
//            // split by space
//            String[] split_msg =  message.split("\\s+");
//
//            // Combine the rest of message
//            StringBuilder actualMessageBuilder = new StringBuilder();
//            for (int i = 1; i < split_msg.length; i++) {
//                actualMessageBuilder.append(split_msg[i]).append(" ");
//            }
//            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
//            String actualMessage = actualMessageBuilder.toString();
//            sendMessageToPArticularUser(destUserName, "[DM from " + username + "]: " + actualMessage);
//            sendMessageToPArticularUser(username, "[DM from " + username + "]: " + actualMessage);
//        }
//        else { // Message to whole chat
//            broadcast(username + ": " + message);
//        }
//    }
//
//    /**
//     * Handles the closure of a WebSocket connection.
//     *
//     * @param session The WebSocket session that is being closed.
//     */
//    @OnClose
//    public void onClose(Session session) throws IOException {
//
//        // get the username from session-username mapping
//        String username = sessionUsernameMap.get(session);
//
//        // server side log
//        logger.info("[onClose] " + username);
//
//        // remove user from memory mappings
//        sessionUsernameMap.remove(session);
//        usernameSessionMap.remove(username);
//
//        // send the message to chat
//        broadcast(username + " disconnected");
//    }
//
//    /**
//     * Handles WebSocket errors that occur during the connection.
//     *
//     * @param session   The WebSocket session where the error occurred.
//     * @param throwable The Throwable representing the error condition.
//     */
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//
//        // get the username from session-username mapping
//        String username = sessionUsernameMap.get(session);
//
//        // do error handling here
//        logger.info("[onError]" + username + ": " + throwable.getMessage());
//    }
//
//    /**
//     * Sends a message to a specific user in the chat (DM).
//     *
//     * @param username The username of the recipient.
//     * @param message  The message to be sent.
//     */
//    private void sendMessageToPArticularUser(String username, String message) {
//        try {
//            usernameSessionMap.get(username).getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            logger.info("[DM Exception] " + e.getMessage());
//        }
//    }
//
//    /**
//     * Broadcasts a message to all users in the chat.
//     *
//     * @param message The message to be broadcasted to all users.
//     */
//    private void broadcast(String message) {
//        sessionUsernameMap.forEach((session, username) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.info("[Broadcast Exception] " + e.getMessage());
//            }
//        });
//    }


}
