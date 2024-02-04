package com.example.Game_Backend.CollaborativeEditing;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


@Controller
@ServerEndpoint("/editing/{sessionID}/{username}")
public class CollaborativeEditing{

    private static AccountRepository accountRepo;

    private static EditingMapRepository editingMapRepo;

    @Autowired
    public void setMessageRepository(AccountRepository repo) {
        accountRepo = repo;  // we are setting the static variable
    }

    @Autowired
    public void setMessageRepository(EditingMapRepository repo) {
        editingMapRepo = repo;  // we are setting the static variable
    }


    private static Map<String, Map<Session, String>> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Map<String, Session>> usernameSessionMap = new Hashtable<>();


    // server side logger
    private final Logger logger = LoggerFactory.getLogger(CollaborativeEditing.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sessionID") String sessionID, @PathParam("username") String username) throws IOException {
        // Get the user from the database using userID
        Account user = accountRepo.findById(Long.parseLong(username)).orElse(null);

        if (user == null) {
            // Handle user not found
            session.close();
            return;
        }

        logger.info("[edit-onOpen] " + user.getUsername());


        // Handle the case of a duplicate user in the same session
        if (usernameSessionMap.containsKey(sessionID) && usernameSessionMap.get(sessionID).containsKey(user.getUsername())) {
            session.getBasicRemote().sendText(user.getUsername() + " is already connected to the Collaborative editing");
            session.close();
            return;
        }

        // Initialize the session maps if not exists
        sessionUsernameMap.putIfAbsent(sessionID, new Hashtable<>());
        usernameSessionMap.putIfAbsent(sessionID, new Hashtable<>());

        // map current session with username
        sessionUsernameMap.get(sessionID).put(session, user.getUsername());
        // map current username with session
        usernameSessionMap.get(sessionID).put(user.getUsername(), session);

//        broadcast(sessionID, user.getUsername()) + "connected");
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param mapContent The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String mapContent, @PathParam("sessionID") String sessionID) throws IOException {
        // Retrieve the username associated with the session
        String username = getUsernameFromSession(session, sessionID);

        // Server-side log
        logger.info("[edit-onMessage] " + (username != null ? username : "Unknown user") + ": " + mapContent);

        // Broadcast the message within the session
        broadcast(sessionID, mapContent);

        // Save the message to the database
        EditingMapHelper mh = new EditingMapHelper(editingMapRepo);
        Long snum = Long.parseLong(sessionID);
        mh.addChanges(snum, mapContent);
    }

    // Utility method to get username from the session
    private String getUsernameFromSession(Session session, String sessionID) {
        Map<Session, String> sessionMap = sessionUsernameMap.get(sessionID);
        if (sessionMap != null && sessionMap.containsKey(session)) {
            return sessionMap.get(session);
        }
        return null; // Return null if the username is not found for the given session
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session, @PathParam("sessionID") String sessionID) throws IOException {
        Map<Session, String> sessionMap = sessionUsernameMap.get(sessionID);

        // Check if the session is in the map
        if (sessionMap != null) {
            String username = sessionMap.get(session);

            if (username != null) {
                // server side log
                logger.info("[edit-onClose] " + username);

                // remove user from memory mappings
                sessionMap.remove(session);
                usernameSessionMap.get(sessionID).remove(username);

                // send the message to chat
                broadcast(sessionID, username + " disconnected from the session");
            }
        }
    }


    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Retrieve the username from the session if available
        String username = getUsernameFromSession(session);

        // Log the error
        logger.error("[edit-onError] " + (username != null ? username : "Unknown user") + ": " + throwable.getMessage());

        // Perform additional error handling as needed
    }

    // Utility method to get username from the session
    private String getUsernameFromSession(Session session) {
        for (Map.Entry<String, Map<Session, String>> entry : sessionUsernameMap.entrySet()) {
            Map<Session, String> sessionMap = entry.getValue();
            if (sessionMap.containsKey(session)) {
                return sessionMap.get(session);
            }
        }
        return null; // Return null if the username is not found for the given session
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */

    private void sendMessageToParticularUser(String sessionID, String username, String message) {
        try {
            Session userSession = usernameSessionMap.get(sessionID).get(username);
            if (userSession != null) {
                userSession.getBasicRemote().sendText(message);
            } else {
                logger.info("[edit-DM Exception] User session not found for username: " + username);
            }
        } catch (IOException e) {
            logger.info("[edit-DM Exception] " + e.getMessage());
        }
    }



    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String sessionID, String message) {
        sessionUsernameMap.get(sessionID).forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[edit-Broadcast Exception] " + e.getMessage());
            }
        });
    }
}

//package com.example.Game_Backend.CollaborativeEditing;
//
//import com.example.Game_Backend.Accounts.Account;
//import com.example.Game_Backend.Accounts.AccountRepository;
//import jakarta.annotation.PostConstruct;
//import jakarta.websocket.*;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
//
//import java.io.IOException;
//import java.util.Hashtable;
//import java.util.Map;
//
//
//@Controller
//@ServerEndpoint("/editing/{username}")
//public class CollaborativeEditing{
//
//    private static AccountRepository accountRepo;
//
//    private static EditingMapRepository editingMapRepo;
//
//    @Autowired
//    public void setMessageRepository(AccountRepository repo) {
//        accountRepo = repo;  // we are setting the static variable
//    }
//
//    @Autowired
//    public void setMessageRepository(EditingMapRepository repo) {
//        editingMapRepo = repo;  // we are setting the static variable
//    }
//
//
//    private static Map <Session, String > sessionUsernameMap = new Hashtable < > ();
//    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();
//
//    // server side logger
//    private final Logger logger = LoggerFactory.getLogger(CollaborativeEditing.class);
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
//        //Here I'm getting the user id inetad of the username, so I'm just converting that string into
//        //a long and then findig the username witht eh respective id
//
//        //here the username will represent the id
//        Long userId = Long.parseLong(username);
//        Account user_name = accountRepo.findById(userId).orElse(null);
//        // server side log
//        logger.info("[onOpen] " + user_name.getUsername());
//
//        // Handle the case of a duplicate username
//        if (usernameSessionMap.containsKey(user_name.getUsername())) {
//            session.getBasicRemote().sendText(user_name.getUsername() + " is already connected to the Collaborative editing" );
//            session.close();
//        }
//
//        else {
//            // map current session with username
//            sessionUsernameMap.put(session, user_name.getUsername());
//            // map current username with session
//            usernameSessionMap.put(user_name.getUsername(), session);
//
//        }
//    }
//
//    /**
//     * Handles incoming WebSocket messages from a client.
//     *
//     * @param session The WebSocket session representing the client's connection.
//     * @param mapContent The message received from the client.
//     */
//    @OnMessage
//    public void onMessage(Session session, String mapContent) throws IOException {
//        // get the username by session
//        String username = sessionUsernameMap.get(session);
//        // server side log
//        logger.info("[onMessage] " + username + ": " + mapContent);
//        broadcast(mapContent);
//
//        EditingMap e = new EditingMap();
//
//        long numberOfSaves = editingMapRepo.count();
//
//        //This will set the id to the last element so it doesnt have trouble whe deleting
//        e.setEditingMap_Id(numberOfSaves + 1);
//
//        e.setBody(mapContent);
//
//        editingMapRepo.save(e);
//
//    }
//
//    /**
//     * Handles the closure of a WebSocket connection.
//     *
//     * @param session The WebSocket session that is being closed.
//     */
//    @OnClose
//    public void onClose(Session session) throws IOException {
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
//        broadcast(username + " disconnected from the session");
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
//
//    //No need
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
//     * @param mapContent The message to be broadcasted to all users.
//     */
//    private void broadcast(String mapContent) {
//        sessionUsernameMap.forEach((session, username) -> {
//            try {
//                session.getBasicRemote().sendText(mapContent);
//            } catch (IOException e) {
//                logger.info("[Broadcast Exception] " + e.getMessage());
//            }
//        });
//    }
//}
