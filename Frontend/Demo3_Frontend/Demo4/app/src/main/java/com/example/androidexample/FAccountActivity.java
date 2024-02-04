package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;


public class FAccountActivity extends AppCompatActivity {

    Button buttonBack;
    private String userId;

    Button buttonToAdd;

    private String friendId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendaccountactivity);
        buttonBack = findViewById(R.id.button_to_main);
        buttonToAdd = findViewById(R.id.button_Add);

         friendId = getIntent().getStringExtra("friendId");
        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(FAccountActivity.this, SearchActivity.class);
            intent.putExtra("userUrl", userUrl);
            startActivity(intent);
        });

        buttonToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to make the POST request
                addFriend();
            }
        });


        fetchImageId();
        fetchFriendData();
        fetchFriendUsername();
    }

    private void fetchFriendData() {

        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + friendId; // Update the URL for fetching friend's data
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        // Assuming the friend's data structure is similar to the user's
                        String friendId = jsonResponse.getString("id");
                        JSONObject inventory = jsonResponse.getJSONObject("inventory");
                        int money = jsonResponse.optInt("money", 0); // Default to 0 if not present

                        // Update the UI with the fetched data
                        updateUI(friendId, inventory, money);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error fetching friend's data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, "Error fetching friend's data", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }


    private void fetchFriendUsername() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + friendId + "/name";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    updateUsernameUI(response.trim());
                },
                error -> Toast.makeText(this, "Error fetching username", Toast.LENGTH_SHORT));

        queue.add(stringRequest);
    }


    private void updateUsernameUI(String username) {
        TextView usernameTextView = findViewById(R.id.username_view); // Replace with your actual TextView ID
        usernameTextView.setText("Username: " + username);
    }

    private void updateUI(String userId, JSONObject inventory, int money) {
        TextView userIdTextView = findViewById(R.id.userid_view);
        userIdTextView.setText("User ID: " + userId);

        TextView item1TextView = findViewById(R.id.Item1_view);
        TextView item2TextView = findViewById(R.id.item2_view);
        TextView item3TextView = findViewById(R.id.item3_view);

        item1TextView.setText(inventory.optString("1", "No item"));
        item2TextView.setText(inventory.optString("2", "No item"));
        item3TextView.setText(inventory.optString("3", "No item"));

        TextView moneyTextView = findViewById(R.id.money_view);
        moneyTextView.setText("Money: " + money);
    }


    private void fetchImageId() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + friendId + "/image";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    int imageId = Integer.parseInt(response.trim());
                    updateImageView(imageId);
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error fetching image ID", Toast.LENGTH_SHORT).show();
                });

        queue.add(stringRequest);
    }



    private void updateImageView(int imageId) {
        ImageView Photo = findViewById(R.id.image_picture);
        switch (imageId) {
            case 1:
                Photo.setImageResource(R.drawable.icon1);
                break;
            case 2:
                Photo.setImageResource(R.drawable.icon2);
                break;

            case 3:
                Photo.setImageResource(R.drawable.icon3);
                break;

            case 4:
                Photo.setImageResource(R.drawable.icon4);
                break;

            default:
                Photo.setImageResource(R.drawable.icon5); // Default or placeholder image
                break;
        }
    }


    private void addFriend() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/fRequest/" + friendId; // Replace with your actual URL
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    Toast.makeText(FAccountActivity.this, "Friend request sent successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(FAccountActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>(); // No parameters are being sent in the body
            }
        };

        queue.add(stringRequest);
    }


}
