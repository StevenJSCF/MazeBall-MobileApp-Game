package com.example.Game_Backend.Chat;

import com.vdurmont.emoji.EmojiParser;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 * *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/directchat/{username}/{contact}" endpoint.
 * *
 * Example URL: ws://localhost:8080/directchat/username/contact
 * *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 */
@ServerEndpoint("/directchat/{username}/{contact}")
@Component
public class DirectChat {

    private static MessageRepository messageRepository;
    private static ChatRepository chatRepository;

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map <Session,String> sessionUsernameMap=new Hashtable<>();
    private static Map <String,Session> usernameSessionMap=new Hashtable<>();

    // Map <Username,Chat>
    private static Map <String,Chat> usernameChatMap=new Hashtable<>();

    // server side logger
    private final Logger logger=LoggerFactory.getLogger(DirectChat.class);

    private static Map<String,String> usernameContactMap=new Hashtable<>();

    @Autowired
    public void setMessageRepository(ChatRepository repo) {
        chatRepository=repo;
    }

    @Autowired
    public void setMessageRepository(MessageRepository repo){
        messageRepository=repo;
    }

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username The accessing user.
     * @param contact The desired contact.
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username,@PathParam("contact") String contact) throws IOException {

        // server side log
        logger.info("[onOpen] "+username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else if (username.equals(contact)) {
            session.getBasicRemote().sendText("Username should not equal contact");
            session.close();
        } else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);

            // map current username with contact
            usernameContactMap.put(username, contact);

            // find corresponding chat record
            if (!chatRepository.existsChatsByPlayersContainsAndPlayersContains(username, contact)) {
                usernameChatMap.put(username, new Chat(username, contact));
                usernameChatMap.put(contact, usernameChatMap.get(username));
                chatRepository.save(usernameChatMap.get(username));
            }else {
                usernameChatMap.put(username,chatRepository.findByPlayersContainsAndPlayersContains(username,contact));
                usernameChatMap.put(contact,usernameChatMap.get(username));
            }

            // SENDS MESSAGE HISTORY
            List<Message> history=usernameChatMap.get(username).getMessages();
            history.forEach((message -> {
                try {
                    // Sends old message in the format: "username messageContent"
                    // Example of user "Paddy" saying "hello!": "Paddy hello!"
                    usernameSessionMap.get(username).getBasicRemote().sendText(message.getUserName()+" "+message.getContent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            // send to the user joining in
            sendMessageToPArticularUser(username,"Welcome to the chat server, "+username);

            // send to the user's contact
            if(usernameSessionMap.containsKey(contact)) {
                sendMessageToPArticularUser(contact, "User: " + username + " has Joined the Chat");
            }
        }
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param rawMessage The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session,String rawMessage) {

        // get the username by session
        String username=sessionUsernameMap.get(session);

        if(!usernameSessionMap.containsKey(usernameContactMap.get(username))){
            sendMessageToPArticularUser(username,"Contact has not connected yet!");
        }else {

            // server side log
            logger.info("[onMessage] " + username + ": " + rawMessage);

            // parse emojis
            String message = EmojiParser.parseToUnicode(rawMessage);

            // send message to chat
            sendMessageToPArticularUser(usernameContactMap.get(username), username + ": " + message);
            sendMessageToPArticularUser(username, username + ": " + message);
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) {

        // get the username from session-username mapping
        String username=sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] "+username);

        // send the message to chat
        sendMessageToPArticularUser(usernameContactMap.get(username),username+" disconnected");

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // remove user contact pair from mappings
        usernameContactMap.remove(username);
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session,Throwable throwable) {

        // get the username from session-username mapping
        String username=sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]"+username+": "+throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    public void sendMessageToPArticularUser(String username,String message) {
        try {
            if(usernameSessionMap.containsKey(username)) {
                if (usernameSessionMap.get(username).isOpen()) {
                    Message message1=new Message(username, message,usernameChatMap.get(username));
                    usernameSessionMap.get(username).getBasicRemote().sendText(message);
                    usernameChatMap.get(username).addMessage(message1);
                    chatRepository.save(usernameChatMap.get(username));
                    messageRepository.save(message1);
                }
            }
        } catch (IOException e) {
            logger.info("[DM Exception] "+e.getMessage());
        }
    }
}