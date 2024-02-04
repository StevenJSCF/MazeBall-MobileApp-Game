package com.example.Game_Backend.UserActivity;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.Player.PlayerController;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint("/active/{id}") //Connect to this endpoint on login!
public class UserActivity {

    private static AccountRepository accountRepository;

    private static Map<Session,String> sessionUsernameMap=new Hashtable<>();
    private static Map<String,Session> usernameSessionMap=new Hashtable<>();

    protected static Map<String, String> usernameStatusMap=new Hashtable<>();

    private static final Logger logger=LoggerFactory.getLogger(UserActivity.class);

    @Autowired
    public void setMessageRepository(AccountRepository repo) {
        accountRepository=repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) {
        Account user=accountRepository.findById(id).orElse(null);

        if (user == null) {
            // Handle user not found
            onClose(session);
            return;
        }

        logger.info("[onOpen]"+user.getUsername());
        sessionUsernameMap.put(session,user.getUsername());
        usernameSessionMap.put(user.getUsername(),session);
        usernameStatusMap.put(user.getUsername(),"Active");
        broadcast(user.getUsername()+" Active");
    }

    @OnMessage  //Proper formatting: "query "+id
    public void onMessage(Session session, String message) {
        String username=sessionUsernameMap.get(session);
        logger.info("[onMessage] "+username+": "+message);
        String[] split_msg=message.split("\\s+");
        String status=split_msg[0];
        Long id=Long.parseLong(split_msg[1]);
        switch (status.toLowerCase()){
            case "active":          active(id);
                break;
            case "away":            away(id);
                break;
            case "donotdisturb":    doNotDisturb(id);
                break;
            case "get":             getStatus(id);
                break;
            case "friends":         friendListStatus(id);
                break;
        }
    }

    @PutMapping("/doNotDisturb/{id}")
    public void doNotDisturb(@PathVariable("id") @Parameter(description = "Player to set status") Long id){
        Account user=accountRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("[Status Update]: Do Not Disturb - "+id);
        usernameStatusMap.replace(user.getUsername(),"Do Not Disturb");
        broadcast(user.getUsername()+" Do Not Disturb");
    }

    @PutMapping("/away/{id}")
    public void away(@PathVariable("id") @Parameter(description = "Player to set status") Long id){
        Account user=accountRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("[Status Update]: Away - "+id);
        usernameStatusMap.replace(user.getUsername(),"Away");
        broadcast(user.getUsername()+" Away");
    }

    @PutMapping("/active/{id}")
    public void active(@PathVariable("id") @Parameter(description = "Player to set status") Long id){
        Account user=accountRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("[Status Update]: Active - "+id);
        usernameStatusMap.replace(user.getUsername(),"Active");
        broadcast(user.getUsername()+" Active");
    }

    @GetMapping("/status/{qid}")   //Use to check status of user
    public String getStatus(@PathVariable("qid") @Parameter(description = "ID in question") Long id){
        Account user=accountRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("[Activity Check]: "+user.getUsername());
        broadcast(user.getUsername()+" "+usernameStatusMap.get(user.getUsername()));
        return usernameStatusMap.get(user.getUsername());
    }

    public static String getStatus(@Parameter String username){
        logger.info("[Activity Check]: "+username);
        return usernameStatusMap.get(username);
    }

    @GetMapping("/friendsActive/{id}")
    public Map<String,String> friendListStatus(@PathVariable("id") @Parameter(description = "ID of Player to get friend list") Long id){
        Account user=accountRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("[FriendActivityCheck]: "+user.getUsername());
        List<String> friendNames= new PlayerController().getFriendNames(id);
        Map<String,String> activityMap = new HashMap<>();
        for(String friend:friendNames){
            if(usernameSessionMap.containsKey(friend)) {
                activityMap.put(friend,usernameStatusMap.get(friend));
            }
        }
        return activityMap;
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("[onClose]"+sessionUsernameMap.get(session));
        broadcast(sessionUsernameMap.get(session)+" Away");
        usernameSessionMap.remove(sessionUsernameMap.get(session));
        sessionUsernameMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("[onError]" + sessionUsernameMap.get(session) + ": " + throwable.getMessage());
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    public void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                if(session.isOpen())
                    session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] "+e.getMessage());
            }
        });
    }

}