package com.example.Game_Backend.Player;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.UserActivity.UserActivity;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint("/friendNotifications/{id}") //Connect to this endpoint on login!
public class FriendNotifications {

    @Autowired
    private static AccountRepository accountRepository;
    @Autowired
    private PlayerRepository playerRepository;

    private static Map<Session,String> sessionUsernameMap=new Hashtable<>();
    private static Map<String,Session> usernameSessionMap=new Hashtable<>();

    private static final Logger logger= LoggerFactory.getLogger(UserActivity.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) {
        Account user=accountRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("[onOpen]"+user.getUsername()); //Console logger
        sessionUsernameMap.put(session,user.getUsername());
        usernameSessionMap.put(user.getUsername(),session);
        List<Long> friendRequests=playerRepository.findById(id).orElseThrow(RuntimeException::new).getFriendInvites();
        ArrayList<String> names=new ArrayList<>();
        friendRequests.forEach((fid)-> names.add(
                accountRepository.findById(fid)
                        .orElseThrow(RuntimeException::new)
                        .getUsername())
        );
        names.forEach((name)->
                sendNotificationToUser(user.getUsername(),name)    // Sends a notification per friend request
        );
    }

    /**
     * Method called by PlayerController.reqFriend(). Sends a notification immediately if requested friend is online.
     * @param id
     * @param fid
     */
    public static void onRequest(@Parameter(description = "Sending request") Long id,
                                 @Parameter(description = "Receiving request") Long fid){
        if(!usernameSessionMap.isEmpty()) {
            String username = accountRepository.findById(id).orElseThrow(RuntimeException::new).getUsername();
            String friendName = accountRepository.findById(fid).orElseThrow(RuntimeException::new).getUsername();
            if (usernameSessionMap.containsKey(friendName)) {
                sendNotificationToUser(friendName, username);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("[onClose]"+sessionUsernameMap.get(session));
        usernameSessionMap.remove(sessionUsernameMap.get(session));
        sessionUsernameMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("[onError]" + sessionUsernameMap.get(session) + ": " + throwable.getMessage());
    }

    private static void sendNotificationToUser(String username, String message) {
        try {
            if(!UserActivity.getStatus(username).equals("Do Not Disturb"))
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] "+e.getMessage());
        }
    }
}
