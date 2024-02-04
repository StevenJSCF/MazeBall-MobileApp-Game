package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class StoreActivity extends AppCompatActivity {

    Button button;
    private TextView messageText; // define message textview variable
    private TextView coinsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store); // link to Main activity XML
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

    private void setCoins(int amount) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("money", amount);
        editor.apply();

        // Update the TextView to reflect the new coin amount
        coinsText.setText(String.valueOf(amount));
    }

    private void incrementItemCount(String itemKey, String itemName) {
        int coins = getCoins();

        if (coins < 10) {
            Toast.makeText(StoreActivity.this, "You do not have enough coins!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int itemCount = prefs.getInt(itemKey, 0);
        itemCount++;

        setCoins(coins - 10);
        updateCoinValueOnServer();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(itemKey, itemCount);
        editor.apply();

        postNewItem(itemName, "10"); // Post the item after buying
    }



    private void fetchAndDisplayCoins(final TextView tvCoinValue) {
        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/4";


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

    private void updateCoinValueOnServer() {

        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/4/money/-10";


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


    private void postNewItem(String itemName, String itemPrice) {

        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/player/4/addItem/3";

        // Create a POST request with Volley
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
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
