package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaderBoard extends AppCompatActivity {

    private static final String TAG = LeaderBoard.class.getSimpleName();
    private RequestQueue requestQueue;
    private LeaderBoard context = this;
    private TextView leaderboardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add a back button to the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        requestQueue = Volley.newRequestQueue(this);


        // Make a Volley request to get leaderboard data
        String url = "http://coms-309-021.cs.iastate.edu:8080/users/Leaderboard"; // Replace with your actual API endpoint


        getLeaderboardData(new LeaderBoard.Callback<String>() {
            @Override
            public void onResponse(String response) {
               String leaderboardData = response;
              ;
                Log.d(TAG, "Received leaderboard data: " + leaderboardData);
                String[] userScorePairs = leaderboardData.split(":");
                LinearLayout leaderboardContainer = findViewById(R.id.leaderboardContainer);

                for (String userScorePair : userScorePairs) {
                    String[] parts = userScorePair.split(",");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String score = parts[1];

                        // Create a new TextView for each entry
                        TextView entryTextView = new TextView(context);
                        entryTextView.setText(username + ": " + score);
                        entryTextView.setTextSize(18);
                        entryTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        entryTextView.setBackground(getResources().getDrawable(R.drawable.border_background));
                        entryTextView.setPadding(16, 8, 16, 8);

                        // Make the font bold
                        entryTextView.setTypeface(null, Typeface.BOLD);

                        // Adjust layout parameters to make the scores take up the entire line
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(0, 8, 0, 8);
                        entryTextView.setLayoutParams(layoutParams);

                        // Add the TextView to the leaderboardContainer
                        leaderboardContainer.addView(entryTextView);
                    }
                }


            }

            @Override
            public void onError(VolleyError error) {
                // Handle the error here
                System.out.println("Error fetching levels from the server");
            }
        });



        // Format the leaderboard data

    }



    // Define a callback interface
    interface Callback<T> {
        void onResponse(T response);
        void onError(VolleyError error);
    }
    private void getLeaderboardData(final LeaderBoard.Callback<String> callback) {
        String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/map/top10LeaderBoard";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            /// Parse the JSON response and format the leaderboard data
                            String leaderboardData = response.getString("message");
                            Log.d(TAG, "Raw Leaderboard Data: " + leaderboardData);

                            // Format the leaderboard data


                            callback.onResponse(leaderboardData);
                            // Display the formatted data in the TextView
                            //leaderboardTextView.setText(formattedData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                        callback.onResponse(error.getMessage());
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
