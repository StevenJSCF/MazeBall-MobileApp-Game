package com.example.myapplication;

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


public class StoreActivity extends AppCompatActivity {

    Button button;
    private TextView messageText; // define message textview variable
    private TextView coinsText;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];
        //userId = getIntent().getStringExtra("userId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        messageText = findViewById(R.id.store_msg_txt); // link to message textview in the Main activity XML
        coinsText = findViewById(R.id.coins);
        fetchAndDisplayCoins(coinsText);
        coinsText.setText(String.valueOf(getCoins())); // Update the TextView with the current coin value

        coinsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCoins = getCoins();
                if (currentCoins >= 10) {
                    setCoins(currentCoins - 10);
                } else {
                    Toast.makeText(StoreActivity.this, "Not enough coins!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        messageText.setText("Store");

        button = findViewById(R.id.toInventoryBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, InventoryActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        Button buyReversButton = findViewById(R.id.buyReversButton);
        buyReversButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementItemCount("revers_item_count", "DS3");

            }
        });



        Button buyTwentyButton = findViewById(R.id.buyTwentyButton);
        buyTwentyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementItemCount("twenty_item_count", "Sekiro");

            }
        });



        Button buyTensButton = findViewById(R.id.buyTensButton);
        buyTensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementItemCount("tenSec_item_count", "DS2");
            }
        });


    }

    private int getCoins() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getInt("money", 100); // Assuming default coins are 100
  }

    /**
     * Set coins amount that sam ea sthe backend
     * @param amount
     */
    private void setCoins(int amount) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("money", amount);
        editor.apply();

        // Update the TextView to reflect the new coin amount
        coinsText.setText(String.valueOf(amount));
    }

    /**
     * Increase item number when you purchase
     * @param itemKey
     * @param itemName
     */
    private void incrementItemCount(String itemKey, String itemName) {
    int coins = getCoins();

    if (coins < 10) {
        Toast.makeText(StoreActivity.this, "You do not have enough coins!", Toast.LENGTH_SHORT).show();
        return;  // Exit the method if not enough coins
    }

    // Deduct coins
    setCoins(coins - 10);
    updateCoinValueOnServer();

    // Increment item count in SharedPreferences
    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    int itemCount = prefs.getInt(itemKey, 0);
    itemCount++;

    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt(itemKey, itemCount);
    editor.apply();

    // Determine the item ID based on the itemKey
    String itemId = "";
    switch (itemKey) {
        case "revers_item_count":
            itemId = "1";
            break;
        case "twenty_item_count":
            itemId = "2";
            break;
        case "tenSec_item_count":
            itemId = "3";
            break;
    }

    // Post the new item to the backend
    postNewItem(itemId);
}


    /**
     *  Use the GET method to request the number of coins from backend
     * @param tvCoinValue
     */


    private void fetchAndDisplayCoins(final TextView tvCoinValue) {

        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Directly get the "coins" value from the root of the JSON object
                            int coins = response.getInt("money");
                            tvCoinValue.setText(String.valueOf(coins));

                            setCoins(coins);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StoreActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StoreActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(StoreActivity.this).add(request);
    }


    /**
     * When you buy something it will decrease the number of coin and will use POST request to the backend to change the numbe rof money
     */

    private void updateCoinValueOnServer() {

        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/"+userId +"/money/-10";


        JSONObject params = new JSONObject();
        try {

            params.put("money", getCoins());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(StoreActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;  // Exit the method if there's a JSON error
        }

        // Create a PUT request with Volley
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                serverUrl,
                params,
                response -> {

                },
                error -> {

                    Toast.makeText(StoreActivity.this, "Request Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );


        Volley.newRequestQueue(StoreActivity.this).add(request);
    }


    /**
     *  Use the item id to send the POST request to the backend to increase the number of particular item
     * @param itemId
     */
    private void postNewItem(String itemId) {
        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/addItem/" + itemId;


        // Create a POST request with Volley
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                serverUrl,
                null,
                response -> {
                    // Handle the successful server response
                    Toast.makeText(StoreActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Display an error message if the request fails
                    Toast.makeText(StoreActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley request queue to execute it
        Volley.newRequestQueue(StoreActivity.this).add(request);
    }


}
