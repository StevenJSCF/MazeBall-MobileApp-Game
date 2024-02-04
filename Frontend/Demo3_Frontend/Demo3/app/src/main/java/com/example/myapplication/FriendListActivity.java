package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class FriendListActivity extends AppCompatActivity {
    private TextView messageText;

    private String userId;

    private JSONArray friendsArray;

    Button buttonFToMain;

    Button buttonListToChat;
    private EditText searchFriendEditText;
    private Button searchFriendButton;
    private Button addFriendButton;
    private JSONArray allUsersArray;


    private UserAdapter usersAdapter;

    private RecyclerView usersRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        messageText = findViewById(R.id.friendList_msg_txt);
        messageText.setText("Friends");

        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];
        searchFriendEditText = findViewById(R.id.searchFriendEditText);
        searchFriendButton = findViewById(R.id.searchFriendButton);
        addFriendButton = findViewById(R.id.addFriendButton);

        buttonFToMain = findViewById(R.id.friends_to_main);
        buttonListToChat = findViewById(R.id.button_list_to_chat);

        usersRecyclerView = findViewById(R.id.friendsRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UserAdapter(new JSONArray());
        usersRecyclerView.setAdapter(usersAdapter);

        buttonFToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Main activity
                Intent intent = new Intent(FriendListActivity.this, MainScreenActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
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

        searchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchFriendEditText.getText().toString();
                if (!searchTerm.isEmpty()) {
                    JSONArray filteredUsers = filterUsers(searchTerm);
                    updateUIWithFilteredUsers(filteredUsers);
                } else {
                    Toast.makeText(FriendListActivity.this, "Please enter a name to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the add friend button click listener
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = searchFriendEditText.getText().toString();
                if (!friendName.isEmpty()) {
                    addNewFriend(friendName);
                } else {
                    Toast.makeText(FriendListActivity.this, "Please enter a friend's name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchAllUsers();
        fetchAllUsersWithIds();
        fetchFriendList();




    }

    /**
     * Use the GEt method to Create a new JSONArray to hold the usernames, update the allUsersArray with the new array of usernames
     * When you search friends it will comes out the friends that might you wnat to add, for example when you hit "p" then you get
     * "Paddy" and "Peter"
     */

    private void fetchAllUsers() {
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/all";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Create a new JSONArray to hold the usernames
                        JSONArray usernamesArray = new JSONArray();

                        // Loop through the original response array
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject userObject = response.getJSONObject(i).getJSONObject("account");

                                String username = userObject.getString("username");


                                JSONObject usernameObject = new JSONObject();
                                usernameObject.put("username", username);


                                usernamesArray.put(usernameObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        allUsersArray = usernamesArray;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendListActivity.this, "Error fetching users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }


    /**
     * Fillter the user through the name
     * @param searchTerm
     * @return
     */
    private JSONArray filterUsers(String searchTerm) {
        JSONArray filteredUsers = new JSONArray();
        if (allUsersArray != null) {
            for (int i = 0; i < allUsersArray.length(); i++) {
                try {
                    JSONObject user = allUsersArray.getJSONObject(i);
                    String username = user.getString("username");
                    if (username.toLowerCase().contains(searchTerm.toLowerCase())) {
                        filteredUsers.put(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return filteredUsers;
    }


    /**
     * Update the User Interface with Filter Users
     * @param filteredUsers
     */
    private void updateUIWithFilteredUsers(JSONArray filteredUsers) {
        if (usersAdapter == null) {

            usersAdapter = new UserAdapter(filteredUsers);
            usersRecyclerView.setAdapter(usersAdapter);
        } else {

            usersAdapter.updateData(filteredUsers);
        }
    }

    /**
     * Add the new friends
     * @param friendName
     */
    private void addNewFriend(String friendName) {
        // First, find the ID of the friendName
        findUserId(friendName, new UserIdCallback() {
            @Override
            public void onUserIdFound(String friendId) {
                // Now that we have the friend's ID, we can send a POST request to add the friend
                sendAddFriendRequest(userId, friendId);
            }

            @Override
            public void onError(VolleyError error) {
                // Handle the error case here
                Toast.makeText(FriendListActivity.this, "Failed to find user ID: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Use the Get request to find the friends and add it
     */

    private void fetchAllUsersWithIds() {
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/all";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("fetchAllUsersWithIds", "User data loaded successfully."); // Add this line
                        allUsersArray = response;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendListActivity.this, "Error fetching users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }




    private void findUserId(String username, UserIdCallback callback) {
        if (allUsersArray != null) {
            Log.d("findUserId", "Searching for user: " + username);
            for (int i = 0; i < allUsersArray.length(); i++) {
                try {
                    JSONObject user = allUsersArray.getJSONObject(i);
                    JSONObject account = user.getJSONObject("account");
                    String currentUsername = account.getString("username");
                    Log.d("findUserId", "Current user: " + currentUsername);
                    if (username.equalsIgnoreCase(currentUsername)) {
                        String friendId = String.valueOf(user.getInt("id"));
                        callback.onUserIdFound(friendId);
                        return;
                    }
                } catch (JSONException e) {
                    Log.e("findUserId", "Error parsing user data", e);
                }
            }
            Log.d("findUserId", "User not found: " + username);
            callback.onError(new VolleyError("User not found"));
        } else {
            Log.d("findUserId", "allUsersArray is null or empty");
            callback.onError(new VolleyError("User data is not loaded"));
        }
    }

    /**
     *  Use the POST request to add the friend and send back to the backend
     * @param userId
     * @param friendId
     */
    private void sendAddFriendRequest(String userId, String friendId) {
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/friends/" + friendId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, serverUrl,
                response -> Toast.makeText(FriendListActivity.this, "Friend added successfully!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(FriendListActivity.this, "Error adding friend: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(postRequest);
    }

    interface UserIdCallback {
        void onUserIdFound(String userId);
        void onError(VolleyError error);
    }


    /**
     * UI of friend list it will generate the friend that you have of everyone
     */
    private void fetchFriendList() {
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

                                // Create a new TableRow
                                TableRow tableRow = new TableRow(FriendListActivity.this);
                                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                // Create a TextView for the friend's name
                                TextView friendTextView = new TextView(FriendListActivity.this);
                                friendTextView.setText(friendName); // Set the friend's name
                                friendTextView.setTextSize(24); // Adjust text size as needed
                                TableRow.LayoutParams textParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                friendTextView.setLayoutParams(textParams);

                                // Add the TextView to the TableRow
                                tableRow.addView(friendTextView);

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


                                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                chatButton.setLayoutParams(buttonParams);

                                // Add the Button to the TableRow
                                tableRow.addView(chatButton);

                                // Add the TableRow to the TableLayout
                                tableLayout.addView(tableRow);
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





//    private void populateFriendsTable(JSONArray friendsArray) {
//        TableLayout tableLayout = findViewById(R.id.friendsTableLayout);
//        tableLayout.removeAllViews(); // Clear previous views if you are calling this method multiple times
//
//        for (int i = 0; i < friendsArray.length(); i++) {
//            try {
//                // Get the friend's name from the array
//                String friendName = friendsArray.getString(i);
//
//                // Create a new TableRow
//                TableRow tableRow = new TableRow(this);
//                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//
//                // Create a TextView for the friend's name
//                TextView friendTextView = new TextView(this);
//                friendTextView.setText(friendName);
//                friendTextView.setTextSize(30);
//                TableRow.LayoutParams textParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
//                friendTextView.setLayoutParams(textParams);
//
//                // Create a Button for chatting
//                Button chatButton = new Button(this);
//                chatButton.setText("Chat");
//                chatButton.setOnClickListener(v -> {
//
//                    // You'll need to pass the friend's ID or name to the chat activity
//                    Intent intent = new Intent(FriendListActivity.this, FriendListActivity.class);
//                    intent.putExtra("friendName", friendName);
//                    // intent.putExtra("friendId", friendId); // If you have the friend's ID
//                    startActivity(intent);
//                });
//                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//                chatButton.setLayoutParams(buttonParams);
//
//                // Add the TextView and Button to the TableRow
//                tableRow.addView(friendTextView);
//                tableRow.addView(chatButton);
//
//                // Add the TableRow to the TableLayout
//                tableLayout.addView(tableRow);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }






}
