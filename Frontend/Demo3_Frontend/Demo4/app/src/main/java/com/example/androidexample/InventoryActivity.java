package com.example.androidexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class InventoryActivity extends AppCompatActivity {




    Button mainButton;
    private TextView messageText;
    private TextView reverseItemCountView;
    private TextView TwentyItemCountView;
    private TextView TensecItemCountView;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        String userUrl = getIntent().getStringExtra("userUrl");
        if (userUrl != null) {
            String[] parts = userUrl.split("/");
            userId = parts[parts.length - 1];
        } else {
           userId = "6";
        }



        messageText = findViewById(R.id.inventory_msg_txt);
        messageText.setText("Inventory");

        reverseItemCountView = findViewById(R.id.reverse_item_count_view);
        TwentyItemCountView = findViewById(R.id.twenty_item_count_view);
        TensecItemCountView = findViewById(R.id.ten_item_count_view);

        mainButton = findViewById(R.id.toMainBtn);


        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });



        //        Button sellReverseButton = findViewById(R.id.sell_reverse);
//        sellReverseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sellItem(userId, "1"); // Assuming "1" is the item ID for the "reverse" item
//            }
//        });
//
//
//        Button sellTensecButton = findViewById(R.id.sell_10s);
//        sellTensecButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sellItem(userId, "3"); // Assuming "1" is the item ID for the "reverse" item
//            }
//        });
//
//        Button sellTwentySec = findViewById(R.id.sell_20);
//        sellTwentySec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sellItem(userId, "2"); // Assuming "1" is the item ID for the "reverse" item
//            }
//        });
//


        fetchAndDisplayInventoryItems();
        updateUI();
    }

    /**
     * Update the counts that you inventory have and when you buy one in the store it will show in the inventory
     */
    public void updateUI() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int ReverseItemCount = prefs.getInt("revers_item_count", 0);
        int TwentyItemCount = prefs.getInt("twenty_item_count", 0);
        int TenItemCount = prefs.getInt("tenSec_item_count", 0);

        reverseItemCountView.setText(String.valueOf(ReverseItemCount));
        TwentyItemCountView.setText(String.valueOf(TwentyItemCount));
        TensecItemCountView.setText(String.valueOf(TenItemCount));
    }

    /**
     * Use the GET request to get the item that you already have in the backend
     */
    public void fetchAndDisplayInventoryItems() {
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the inventory JSONObject from the root of the JSON object
                            JSONObject inventory = response.getJSONObject("inventory");

                            // Extract each individual item count and update the respective TextView
                            int ReverseItemCount = inventory.getInt("1");
                            int TwentyItemCount = inventory.getInt("2");
                            int TenItemCount = inventory.getInt("3");

                            reverseItemCountView.setText(String.valueOf(ReverseItemCount));
                            TwentyItemCountView.setText(String.valueOf(TwentyItemCount));
                            TensecItemCountView.setText(String.valueOf(TenItemCount));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InventoryActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InventoryActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(InventoryActivity.this).add(request);
    }



    //    private void sellItem(String userId, String itemId) {
//        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/addItem/" + itemId + "/sell";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, serverUrl, null,
//                response -> {
//                    updateItemCountInPreferences(itemId, -1);
//                    updateUI();
//                    fetchAndDisplayInventoryItems();
//                },
//                error -> {
//                    // Handle error
//                    Toast.makeText(InventoryActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//
//        // Add the request to the Volley request queue
//        Volley.newRequestQueue(InventoryActivity.this).add(request);
//    }

//    private void updateItemCountInPreferences(String itemId, int change) {
//        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        // Determine which item count to update
//        String key;
//        switch (itemId) {
//            case "1":
//                key = "revers_item_count";
//                break;
//            case "2":
//                key = "twenty_item_count";
//                break;
//            case "3":
//                key = "tenSec_item_count";
//                break;
//            default:
//                return; // Invalid item ID
//        }
//
//        int currentCount = prefs.getInt(key, 0);
//        int newCount = Math.max(0, currentCount + change); // Ensure the count doesn't go below 0
//        editor.putInt(key, newCount);
//        editor.apply();
//    }



}
