package com.example.androidexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.EditText;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONObject;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class FriendListActivity extends AppCompatActivity {
    private TextView messageText;

    private HashMap<String, Long> nameToIdMap = new HashMap<>();


    private List<Long> friendIdList = new ArrayList<>();

    private String userId;

    private JSONArray friendsArray;

    private Context context;

    Button buttonFToMain;

    Button buttonListToChat;
    private EditText searchFriendEditText;

    private JSONArray allUsersArray;


    private UserAdapter usersAdapter;
    private String userUrl;

    ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        context = this;
//        adapter = ArrayAdapter.createFromResource(this,
//                R.array.friends_options, android.R.layout.simple_spinner_item);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];
        searchFriendEditText = findViewById(R.id.searchFriendEditText);


        buttonFToMain = findViewById(R.id.friends_to_main);
        buttonListToChat = findViewById(R.id.button_list_to_chat);


        usersAdapter = new UserAdapter(new JSONArray());


        EditText searchFriendEditText = findViewById(R.id.searchFriendEditText);


        searchFriendEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FriendListActivity.this, SearchActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });


        buttonFToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendListActivity.this, MainScreenActivity.class);
                intent.putExtra("userUrl", userUrl);
               finish();


            }
        });

        buttonListToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the name from the server
                String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/name";
                RequestQueue queue = Volley.newRequestQueue(FriendListActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Navigate to GroupChat activity with the name
                                Intent intent = new Intent(FriendListActivity.this, GroupChatActivity.class);
                                intent.putExtra("CurrentName", response.trim()); // Pass the name to the GroupChatActivity
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(FriendListActivity.this, "Error fetching name: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });




        fetchFriendIds();
        fetchFriendList();


    }











    /**
     * UI of friend list it will generate the friend that you have of everyone
     */
    private void fetchFriendList() {


        Log.d("FriendListActivity", "");

        // The URL for fetching the friend's names
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/friends/names";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a StringRequest to get the friend's names as a JSON array
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray friendsNames = new JSONArray(response);
                            // Get the TableLayout reference
                            TableLayout tableLayout = findViewById(R.id.friendsTableLayout);
                            tableLayout.removeAllViews(); // Clear previous views
                            // Loop through all items in the JSONArray
                            for (int i = 0; i < friendsNames.length(); i++) {
                                // Get the friend's name
                                String friendName = friendsNames.getString(i);
                                TableRow tableRow = new TableRow(FriendListActivity.this);
                                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                // Create and add the status indicator ImageView
                                ImageView statusIndicator = new ImageView(FriendListActivity.this);
                                int statusIndicatorId = View.generateViewId(); // Generate a unique ID
                                statusIndicator.setId(statusIndicatorId); // Set the generated ID
                                statusIndicator.setImageResource(R.drawable.ic_initial_dot); // Set initial image
                                TableRow.LayoutParams imageParams = new TableRow.LayoutParams(100, 100);
                                statusIndicator.setLayoutParams(imageParams);
                                statusIndicator.setTag(friendName); // Set tag for later identification
                                tableRow.addView(statusIndicator);

                                // Create a TextView for the friend's name
                                TextView friendTextView = new TextView(FriendListActivity.this);
                                friendTextView.setText(friendName); // Set the friend's name
                                friendTextView.setTextSize(24); // Adjust text size as needed
                                TableRow.LayoutParams textParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                friendTextView.setLayoutParams(textParams);

                                // Add the TextView to the TableRow
                                tableRow.addView(friendTextView);

                                Spinner userSp = new Spinner(FriendListActivity.this);
                                userSp.setAdapter(adapter);
                                userSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedItem = parent.getItemAtPosition(position).toString();
                                        if (selectedItem.equals("Detail")) {
                                            String friendName = ((TextView) view).getText().toString();
                                            Long friendId = nameToIdMap.get(friendName);
                                            Intent intent = new Intent(FriendListActivity.this, FAccountActivity.class);
                                            intent.putExtra("friendId", friendId.toString());
                                            intent.putExtra("userUrl", userUrl);
                                            startActivity(intent);

                                        } else if (selectedItem.equals("Delete")) {


                                            View row = (View) parent.getParent();
                                            if (row instanceof TableRow) {
                                                TextView friendTextView = (TextView) ((TableRow) row).getChildAt(1); // Assuming the TextView with the friend's name is at index 0
                                                String friendName = friendTextView.getText().toString();
                                                Long friendId = nameToIdMap.get(friendName);

                                                if (friendId != null) {
                                                    deleteFriend(friendId); // Call the delete method
                                                } else {
                                                    Toast.makeText(FriendListActivity.this, "Error: Friend ID not found for " + friendName, Toast.LENGTH_SHORT).show();
                                                }
                                            }




                                        } else if (selectedItem.equals("Block")) {

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Another callback interface.
                                    }
                                });
                                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                userSp.setLayoutParams(buttonParams);
                                // Add the Button to the TableRow
                                tableRow.addView(userSp);


                                // Create a Button for chatting
                                Button chatButton = new Button(FriendListActivity.this);
                                chatButton.setText("Chat");
                                chatButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Fetch the current user's name
                                        String currentUserUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/name";
                                        RequestQueue currentUserQueue = Volley.newRequestQueue(FriendListActivity.this);
                                        StringRequest currentUserRequest = new StringRequest(Request.Method.GET, currentUserUrl,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String currentUserResponse) {
                                                        // Navigate to GroupChat activity with the current user's name and the friend's name
                                                        Intent intent = new Intent(FriendListActivity.this, FriendChatActivivty.class);
                                                        intent.putExtra("CurrentName", currentUserResponse.trim());
                                                        intent.putExtra("friendName", friendName.trim()); // Pass the friend's name
                                                        startActivity(intent);
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Handle error
                                                Toast.makeText(FriendListActivity.this, "Error fetching current user name: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        currentUserQueue.add(currentUserRequest);
                                    }
                                });
//                               TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                chatButton.setLayoutParams(buttonParams);
                                // Add the Button to the TableRow
                                tableRow.addView(chatButton);
                                // Add the TableRow to the TableLayout
                                tableLayout.addView(tableRow);
                                checkAndUpdateFriendStatus(friendName, statusIndicatorId, queue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FriendListActivity.this, "Error parsing friends list", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(FriendListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void checkAndUpdateFriendStatus(String userId, int statusIndicatorId, RequestQueue queue) {
        String statusUrl = "http://coms-309-021.class.las.iastate.edu:8080/status/" + userId;

        // Create a StringRequest for the GET request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, statusUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean isOnline = response.equals("Online"); // Assuming the status is "Online" when active
                        runOnUiThread(() -> {
                            ImageView statusIndicator = findViewById(statusIndicatorId);
                            if (statusIndicator != null && statusIndicator.getTag().equals(userId)) {
                                statusIndicator.setImageResource(isOnline ? R.drawable.ic_green_dot : R.drawable.ic_red_dot);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(FriendListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }



    private JSONArray friendIdsArray; // Declare this as a member variable

    private void fetchFriendIds() {
        // The URL for fetching the friend's IDs
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/friends";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a StringRequest to get the friend's IDs as a JSON array
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            friendIdsArray = new JSONArray(response);
                            onDataFetched(); // Call this method to check if both data fetches are complete
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FriendListActivity.this, "Error parsing friend IDs", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(FriendListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void mapNamesToIds(JSONArray friendNames, JSONArray friendIds) {

        Log.d("NameToIdMap", "Mapping names to IDs");
        try {
            for (int i = 0; i < friendNames.length(); i++) {
                String name = friendNames.getString(i).trim();
                Long id = friendIds.getLong(i);
                nameToIdMap.put(name, id);
            }

            // Log the contents of the map
            for (Map.Entry<String, Long> entry : nameToIdMap.entrySet()) {
                Log.d("NameToIdMap", "Name: " + entry.getKey() + ", ID: " + entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(FriendListActivity.this, "Error mapping names to IDs", Toast.LENGTH_SHORT).show();
        }
    }




    private int dataFetchCounter = 0;

    private synchronized void onDataFetched() {
        dataFetchCounter++;
        if (dataFetchCounter == 2) { // Assuming 2 is the number of data fetch operations
            mapNamesToIds(friendsArray, friendIdsArray);
        }
    }


    public class CustomDeleteRequest extends StringRequest {

        public CustomDeleteRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        public int getMethod() {
            return Method.DELETE;
        }
    }



    private void deleteFriend(Long friendId) {
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/friends/" + friendId;
        RequestQueue queue = Volley.newRequestQueue(this);

        CustomDeleteRequest deleteRequest = new CustomDeleteRequest(Request.Method.DELETE, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Toast.makeText(FriendListActivity.this, "Friend deleted successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, refresh the friend list here if needed
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(FriendListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(deleteRequest);
    }






}
