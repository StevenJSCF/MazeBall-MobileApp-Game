package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {
    private TextView messageText;

    Button buttonToMain;

    private String userId;


    @Override
        protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        messageText = findViewById(R.id.account_msg_txt);
        messageText.setText("Profile");



        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];


        buttonToMain = findViewById(R.id.button_to_main);
/**
 * Button that goes to main
 */
        buttonToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Inventory activity
                Intent intent = new Intent(AccountActivity.this, MainScreenActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        fetchUserData();


    }


    /**
     * Use the GET to request a string response from the provided URL.
     */
    private void fetchUserData() {

        String url = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/" + userId;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String userId = jsonResponse.getString("id");
                        JSONObject inventory = jsonResponse.getJSONObject("inventory");
                        JSONObject account = jsonResponse.getJSONObject("account");


                        // Update the UI with the fetched data
                        updateUI(userId, inventory, account);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    /**
     *  Get the Item in the inventory from backend
     * @param userId
     * @param inventory
     * @param account
     */
    private void updateUI(String userId, JSONObject inventory, JSONObject account) {
        TextView userIdTextView = findViewById(R.id.userid_view); // Update the ID to match your XML
        userIdTextView.setText("User ID: " + userId);

        // Assuming you have TextViews for the first three inventory items
        TextView item1TextView = findViewById(R.id.Item1_view);
        TextView item2TextView = findViewById(R.id.item2_view);
        TextView item3TextView = findViewById(R.id.item3_view);

        // Set the text for each TextView with the item quantity or "No item" if it's not present
        item1TextView.setText(inventory.optString("1", "No item"));
        item2TextView.setText(inventory.optString("2", "No item"));
        item3TextView.setText(inventory.optString("3", "No item"));

        // Display username from the account object
        TextView usernameTextView = findViewById(R.id.username_view); // Make sure you have a TextView with this ID in your XML
        usernameTextView.setText("Username: " + account.optString("username", "N/A"));

        // Display money
//        TextView moneyTextView = findViewById(R.id.money_view); // Make sure you have a TextView with this ID in your XML
//       moneyTextView.setText("Money: " + account.optString("money", "0"));


    }


}
