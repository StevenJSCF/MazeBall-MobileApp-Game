package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;


public class FriendChatActivivty extends AppCompatActivity implements WebSocketListener{


    private TextView messageText;

    Button buttonToFriendList;

    private Button sendBtn;

    private TextView msgTv;


    private String friendUser;

    private String currentUser;

    private EditText msgEtx;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();





    //private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private String BASE_URL = "ws://coms-309-021.class.las.iastate.edu:8080/directchat/";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendchat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);

        messageText = findViewById(R.id.friendChat_msg_txt);
        messageText.setText("FriendChat");



        sendBtn = (Button) findViewById(R.id.bt2);

        msgEtx = (EditText) findViewById(R.id.et2);


       String userUrl = getIntent().getStringExtra("userUrl");
       currentUser = getIntent().getStringExtra("CurrentName");
        friendUser = getIntent().getStringExtra("friendName");




        String serverUrl = BASE_URL + currentUser + "/" + friendUser;


        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(FriendChatActivivty.this);



        buttonToFriendList = findViewById(R.id.button_to_friend_list);


        buttonToFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Account activity
                Intent intent = new Intent(FriendChatActivivty.this, FriendListActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }

        });





        sendBtn.setOnClickListener(v -> {
            try {

                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });



    }

    /**
     * Create a Message object and add it to the list
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            // Create a Message object and add it to the list
            messageList.add(new Message(message, false)); // false assuming it's not from the user
            chatAdapter.notifyDataSetChanged(); // Let the adapter know of the new message
            chatRecyclerView.smoothScrollToPosition(messageList.size() - 1); // Scroll to the bottom
        });
    }


    /**
     * When someone cancel the chat he will close the
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }


    /**
     *
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    /**
     *
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {}



}
