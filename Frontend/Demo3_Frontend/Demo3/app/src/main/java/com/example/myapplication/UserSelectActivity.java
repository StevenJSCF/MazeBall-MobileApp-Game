package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class UserSelectActivity extends AppCompatActivity {
    private TextView messageText;
    private Button user1Button, user2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * When hot the different button it will go to the different user's information
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userselect);
        messageText = findViewById(R.id.userSelect_msg_txt);
        messageText.setText("Select user you want to join");

        user1Button = findViewById(R.id.user1_button);
        user2Button = findViewById(R.id.user2_button);

        user1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSelectActivity.this, MainScreenActivity.class);
                intent.putExtra("userUrl", "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/6");
                startActivity(intent);
            }
        });

        user2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSelectActivity.this, MainScreenActivity.class);
                intent.putExtra("userUrl", "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/7");
                startActivity(intent);
            }
        });
    }


    private void fetchUserData(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Here you get the response from the server
                        // For example, you can display it in a TextView
                        messageText.setText("Response: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                messageText.setText("Failed to fetch data!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
