package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;


public class UserSelectActivity extends AppCompatActivity implements WebSocketListener {
    private TextView messageText;
    private Button user1Button, user2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userselect);
        messageText = findViewById(R.id.userSelect_msg_txt);
        messageText.setText("Select user you want to join");

        user1Button = findViewById(R.id.user1_button);
        user2Button = findViewById(R.id.user2_button);

        user1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToUser("6");
            }
        });

        user2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToUser("7");
            }
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
//            String closedBy = remote ? "server" : "local";
//            runOnUiThread(() -> {
//                String s = msgTv.getText().toString();
//                msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//            });
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

    public void onWebSocketMessage(String message) {}


    @Override
    public void onWebSocketConnectionSuccess() {
        runOnUiThread(() -> Toast.makeText(UserSelectActivity.this, "WebSocket connected successfully!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWebSocketConnectionFailure(Exception ex) {
        runOnUiThread(() -> Toast.makeText(UserSelectActivity.this, "WebSocket connection failed: " + ex.getMessage(), Toast.LENGTH_LONG).show());
    }







        private void connectToUser(String userId) {
        String userUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId;
        String userWebSocketUrl = "ws://coms-309-021.class.las.iastate.edu:8080/active/" + userId;

        // Set the WebSocket listener for connection status
        WebSocketManager.getInstance().setWebSocketListener(this);


        // Connect to the WebSocket
        WebSocketManager.getInstance().connectWebSocket(userWebSocketUrl);

        // Proceed with the intent to start MainScreenActivity
        Intent intent = new Intent(UserSelectActivity.this, MainScreenActivity.class);
        intent.putExtra("userUrl", userUrl);
        intent.putExtra("userWebSocketUrl", userWebSocketUrl);
        startActivity(intent);
    }





}
