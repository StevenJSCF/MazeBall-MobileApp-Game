package com.example.androidexample;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import com.android.volley.toolbox.JsonArrayRequest;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;





public class NotificationActivity extends AppCompatActivity {

    private String userId;
    Button backTomain;

    private List<Long> friendIds = new ArrayList<>();


    private RecyclerView recyclerViewInvites;
    private InviteAdapter inviteAdapter;

    private List<String> usernames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_notification);


        backTomain = findViewById(R.id.button_back_M);


        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];

        backTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Main activity
                Intent intent = new Intent(NotificationActivity.this, MainScreenActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);



            }
        });

        recyclerViewInvites = findViewById(R.id.recyclerView_invites);
        recyclerViewInvites.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        inviteAdapter = new InviteAdapter(this, usernames, friendIds);
        recyclerViewInvites.setAdapter(inviteAdapter);


        fetchFriendInvites();






    }




    private void fetchFriendInvites() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/invites";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        friendIds.clear(); // Clear existing data
                        for (int i = 0; i < response.length(); i++) {
                            long friendId = response.getLong(i);
                            friendIds.add(friendId);
                            fetchFriendUsername(friendId);

                        }
                        // Here you can call a method to update your UI or handle the friend IDs as needed
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing invites", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching invites", Toast.LENGTH_SHORT).show());

        queue.add(jsonArrayRequest);
    }




    private void fetchFriendUsername(long friendId) {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + friendId + "/name";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    String username = response.trim();
                    usernames.add(username);
                    inviteAdapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Error fetching username for ID: " + friendId, Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    public void acceptFriendRequest(long friendId) {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/fAccept/" + friendId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    Toast.makeText(this, "Friend request accepted", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error in accepting friend request", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                return null; // No body for this request
            }
        };

        queue.add(postRequest);
    }


    public void declineFriendRequest(long friendId) {
        // Assuming userId is a member variable of NotificationActivity
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/fDecline/" + friendId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Friend request declined", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(this, "Error in declining friend request", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                return null; // No body for this request
            }
        };

        queue.add(postRequest);
    }




}
