package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.io.ByteArrayOutputStream;



import org.java_websocket.handshake.ServerHandshake;


public class GroupChatActivity extends AppCompatActivity implements WebSocketListener{


    private TextView messageText;

    Button buttonToFriendList;

    private Button sendBtn;

    private TextView msgTv;

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();


    private String currentUser;
    private EditText msgEtx;



    //private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private String BASE_URL = "ws://coms-309-021.class.las.iastate.edu:8080/chat/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);

        messageText = findViewById(R.id.friendChat_msg_txt);
        messageText.setText("GroupChat");



        sendBtn = (Button) findViewById(R.id.bt2);

        msgEtx = (EditText) findViewById(R.id.et2);


        String userUrl = getIntent().getStringExtra("userUrl");
        currentUser = getIntent().getStringExtra("CurrentName");




        String serverUrl = BASE_URL + currentUser;


        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(GroupChatActivity.this);



        buttonToFriendList = findViewById(R.id.button_to_friend_list);


        buttonToFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GroupChatActivity.this, FriendListActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }

        });








/**
 * It will set the message box that you send is green and the message box that you recevied is white
 */

        sendBtn.setOnClickListener(v -> {
            try {
                String messageContent = msgEtx.getText().toString();
                if (!messageContent.isEmpty()) {
                    // Create a Message object for the current user
                    Message message = new Message(messageContent, true); // true for isCurrentUser
                    messageList.add(message);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                    msgEtx.setText(""); // Clear the input field
                    // send message to the server
                    WebSocketManager.getInstance().sendMessage(messageContent);
                }
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });


    }


    @Override
    public void onWebSocketMessage(String message){
        runOnUiThread(() -> {
            // Create a Message object and add it to the list
            messageList.add(new Message(message, false)); // false assuming it's not from the user
            chatAdapter.notifyDataSetChanged(); // Let the adapter know of the new message
            chatRecyclerView.smoothScrollToPosition(messageList.size() - 1); // Scroll to the bottom
        });
    }



    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }







    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}


    @Override
    public void onWebSocketConnectionSuccess() {
        // Handle WebSocket connection success event
        runOnUiThread(() -> Toast.makeText(GroupChatActivity.this, "WebSocket connected successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWebSocketConnectionFailure(Exception ex) {
        // Handle WebSocket connection failure event
        runOnUiThread(() -> Toast.makeText(GroupChatActivity.this, "WebSocket connection failed: " + ex.getMessage(), Toast.LENGTH_LONG).show());
    }







}
