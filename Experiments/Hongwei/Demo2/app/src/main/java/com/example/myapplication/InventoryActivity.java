package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;




import androidx.appcompat.app.AppCompatActivity;

public class InventoryActivity extends AppCompatActivity {




    Button mainButton;
    private TextView messageText;
    private TextView reverseItemCountView;
    private TextView TwentyItemCountView;
    private TextView TensecItemCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);



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
                fetchAndDisplayInventoryItems();
            }
        });

        updateUI();
    }

    private void updateUI() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int ReverseItemCount = prefs.getInt("revers_item_count", 0);
        int TwentyItemCount = prefs.getInt("twenty_item_count", 0);
        int TenItemCount = prefs.getInt("tenSec_item_count", 0);

        reverseItemCountView.setText(String.valueOf(ReverseItemCount));
        TwentyItemCountView.setText(String.valueOf(TwentyItemCount));
        TensecItemCountView.setText(String.valueOf(TenItemCount));
    }


    private void fetchAndDisplayInventoryItems() {
        String serverUrl = "http://hongweiw@coms-309-021.class.las.iastate.edu:8080/getItemById/4";

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


//    @Override
//    protected void onResume() {
//        super.onResume();
//        updateUI();  // update UI when returning to this activity
//    }
}
