package com.example.androidexample;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;


public class MainScreenActivity extends AppCompatActivity implements WebSocketListener{


    private TextView messageText;

    Button buttonToStore;
    Button buttonToInventory;

    Button buttonToAccount;

    Button buttonToFriends;

    Button buttonToNotification;

    private String userId;

    private String BASE_URL = "ws://coms-309-021.class.las.iastate.edu:8080/active/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mainscreen);
        messageText = findViewById(R.id.mainscreen_msg_txt);
        messageText.setText("Main");



        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];
        Button back = findViewById(R.id.Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Store activity
                Intent intent = new Intent(MainScreenActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });


        buttonToStore = findViewById(R.id.button_to_store);
        buttonToInventory = findViewById(R.id.button_to_inventory);
        buttonToAccount = findViewById(R.id.button_to_account);
        buttonToFriends = findViewById(R.id.button_to_friends);
        buttonToNotification = findViewById(R.id.button_to_notification);

        buttonToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Store activity
                Intent intent = new Intent(MainScreenActivity.this, StoreActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        buttonToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Inventory activity
                Intent intent = new Intent(MainScreenActivity.this, InventoryActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        buttonToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Account activity
                Intent intent = new Intent(MainScreenActivity.this, AccountActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        buttonToFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FriendList activity
                Intent intent = new Intent(MainScreenActivity.this, FriendListActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });


        buttonToNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Account activity
                Intent intent = new Intent(MainScreenActivity.this, NotificationActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });



        String serverUrl = BASE_URL + userId;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MainScreenActivity.this);


    }









    /**
     * When someone cancel the chat he will close the
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
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
//        runOnUiThread(() -> Toast.makeText(MainScreenActivity.this, "WebSocket connected successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWebSocketConnectionFailure(Exception ex) {
//        runOnUiThread(() -> Toast.makeText(MainScreenActivity.this, "WebSocket connection failed: " + ex.getMessage(), Toast.LENGTH_LONG).show());
    }



}
