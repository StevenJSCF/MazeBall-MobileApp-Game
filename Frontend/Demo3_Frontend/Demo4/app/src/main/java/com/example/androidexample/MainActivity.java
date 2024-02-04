package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity implements NotificationHandler {

    private EditText editTextUsername, editTextPassword;
    private final String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/account/login1"; // Replace with your JSON server URL
private static String ID;
    private WebSocketManager webSocketManager;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Initialize WebSocketManager
        webSocketManager = WebSocketManager.getInstance();

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Create Account" button click
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent intent = new Intent(MainActivity.this, MainMenu.class);
                //startActivity(intent);
                // Get username and password from EditText fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Create a JSON object with login credentials
                JSONObject credentials = new JSONObject();
                try {
                    credentials.put("username", username);
                    credentials.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send a Volley POST request to your JSON server
                System.out.println("Sending request");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverUrl, credentials,
                        new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());

                                try {
                                    String message = response.getString("message");
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    ID = String.valueOf(Integer.parseInt(message));

                                    if (ID.equals("-1")) {
                                        // Handle login failure
                                        System.out.println("Login Failed");
                                    } else {
                                        // Handle login success

                                        // Connect to WebSocket after successful login
                                        // Initialize WebSocketManager and set the listener
                                        //
                                        Intent serviceIntent = new Intent(context, MyBackgroundService.class);
                                        context.startService(serviceIntent);


                                        // NotificationThread notificationThread = new NotificationThread();
                                       //notificationThread.start();
                                        // Navigate to MainMenu activity
                                        Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                        startActivity(intent);
                                        System.out.println("Login Successful");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response here
                                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println("Error: " + error.getMessage());
                            }
                        });

                // Add the request to the Volley request queue
                Volley.newRequestQueue(MainActivity.this).add(request);
            }


        });



    }
    public static String getID(){
        return ID;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect WebSocket when the activity is destroyed
    }

    @Override
    public void onNotificationReceived(String notificationMessage) {
        // Handle notification received event here
        System.out.println("Notification received: " + notificationMessage);
    }
}
