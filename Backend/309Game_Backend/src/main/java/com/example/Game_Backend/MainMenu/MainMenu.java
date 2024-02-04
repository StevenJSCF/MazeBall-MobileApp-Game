package com.example.Game_Backend.MainMenu;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/mainMenu/{username}")
@Component
public class MainMenu {

    private static AccountRepository accountRepo;

    @Autowired
    public void setMessageRepository(AccountRepository repo) {
        accountRepo = repo;  // we are setting the static variable
    }

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String > sessionUsernameMap = new Hashtable< >();
    private static Map <String, Session> usernameSessionMap = new Hashtable < > ();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(MainMenu.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        // server side log

        Account user = accountRepo.findById(Long.parseLong(username)).orElse(null);

        if (user == null) {
            // Handle user not found
            session.close();
            return;
        }

        logger.info("[main-onOpen] user connected to the game:  " + user.getUsername());

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(user.getUsername())) {
            session.getBasicRemote().sendText("User is already connected");
            session.close();
        }
        else {
            // map current session with username
            sessionUsernameMap.put(session, user.getUsername());

            // map current username with session
            usernameSessionMap.put(user.getUsername(), session);
        }
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        String [] splitValue = message.split(",");

        String sender_Id = splitValue[0];
        String receiver_username = splitValue[1];
        String gameMode = splitValue[2];
        String mapId = splitValue[3];

        Account sender_account = accountRepo.findById(Long.parseLong(sender_Id)).orElse(null);
        Account reciever_account = accountRepo.findByUsername(receiver_username);

        if(sender_account == null){// Handle user not found
            session.close();
            return;
        }

        // server side log
        logger.info("[main-onMessage] This user: " + sender_account.getUsername() + " invited ->  " + reciever_account.getUsername());

        String receiver_message = String.valueOf(reciever_account.getId()) + "," + String.valueOf(sender_account.getId()) + "," + sender_account.getUsername() + "," + gameMode + "," + mapId;

        //Here the user who is reciving the message will recieve a message with the user of the person inviting
//        inviteFriend(reciever_account.getUsername(), receiver_message);
      
        broadcast(receiver_message);
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[main-onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // send the message to chat
        broadcast(username + " disconnected");
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[main-onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void inviteFriend(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[main-Invitation Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[main-Broadcast Exception] " + e.getMessage());
            }
        });
    }
}
