package com.example.androidexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



public class LevelList extends AppCompatActivity {

    private int currentPage = 1; // Track the current page
    private int levelsPerPage = 10; // Number of levels per page
    private Integer totalLevels ; // Initialize with 0
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        IntentFilter filter = new IntentFilter(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE);
        registerReceiver(websocketMessageReceiver, filter);


        final LinearLayout buttonContainer = findViewById(R.id.buttonContainer);
        final ScrollView scrollView = findViewById(R.id.scrollView);
        final Button pageUpButton = findViewById(R.id.pageUpButton);
        final Button pageDownButton = findViewById(R.id.pageDownButton);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add a back button to the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // Fetch the total number of levels from the fetchLevels method
          fetchLevels(new Callback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                // Handle the response here
                System.out.println("Received levels: " + response);
                totalLevels = response;
                addButtonsToPage(buttonContainer, 1);

            }

            @Override
            public void onError(VolleyError error) {
                // Handle the error here
                System.out.println("Error fetching levels from the server");
            }
        });
         // totalLevels = 1;


        //addButtonsToPage(buttonContainer, 1);



        // Initialize the first page of buttons

        // Page Up button click listener
        pageUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    buttonContainer.removeAllViews(); // Clear existing buttons
                    addButtonsToPage(buttonContainer, currentPage);
                    scrollView.fullScroll(ScrollView.FOCUS_UP); // Scroll to the top
                }
            }
        });

        // Page Down button click listener
        pageDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalPages = (int) Math.ceil((double) totalLevels / levelsPerPage);
               // System.out.println(totalPages);
                if (currentPage < totalPages) {
                    currentPage++;
                    buttonContainer.removeAllViews(); // Clear existing buttons
                    addButtonsToPage(buttonContainer, currentPage);
                    scrollView.fullScroll(ScrollView.FOCUS_UP); // Scroll to the top
                }
            }
        });
    }

    private void fetchLevels(final Callback<Integer> callback) {
        System.out.println("Fetching levels from the server");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapInfo/getAmmountOfLevels";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String levels = response.getString("levels");
                            int temp = Integer.parseInt(levels);
                            //System.out.println(temp);
                            callback.onResponse(temp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LevelList", "Error fetching levels from the server");
                        callback.onError(error);
                    }
                }
        );

        queue.add(request);
    }

    // Define a callback interface
    interface Callback<T> {
        void onResponse(T response);
        void onError(VolleyError error);
    }



    private void addButtonsToPage(LinearLayout buttonContainer, int page) {
        int startLevel = (page - 1) * levelsPerPage + 1;
        int endLevel = Math.min(page * levelsPerPage, totalLevels);
        //System.out.println(totalLevels);

        for (int i = startLevel; i <= endLevel; i++) {
            Button levelButton = new Button(this);
            levelButton.setText("Level " + i);


            final int selectedLevel = i;
            levelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/map/newMap/post/"+selectedLevel+"/"+ MainActivity.getID();

                    StringRequest request = new StringRequest(Request.Method.POST, url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle the error response here
                                    System.out.println("Error: " + error);
                                }
                            }
                    );
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(request);

                    Intent intent = new Intent(LevelList.this, LevelScreen.class);
                    intent.putExtra("selectedLevel", selectedLevel);
                    startActivity(intent);
                }
            });

            buttonContainer.addView(levelButton);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        return true;
    }
    private final BroadcastReceiver websocketMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE)) {
                String message = intent.getStringExtra(MyBackgroundService.EXTRA_WEBSOCKET_MESSAGE);
                // Display the message on your UI
                System.out.println("Received messagesss: " + message);
                Drawnotification.showGameInvitationDialog(context, message);


            }
        }
    };
}


