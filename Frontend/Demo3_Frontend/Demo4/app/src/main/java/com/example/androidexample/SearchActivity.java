package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import android.widget.Toast;
import android.widget.EditText;
import org.json.JSONObject;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import com.android.volley.toolbox.JsonObjectRequest;







public class SearchActivity extends AppCompatActivity {

    private JSONArray allUsersArray;
    private Button buttonSToF;
    private ArrayAdapter<Friend> friendAdapter;
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];




        ListView listView = findViewById(R.id.listview);
        friendAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(friendAdapter);

        buttonSToF = findViewById(R.id.ToFbutton);
        buttonSToF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, FriendListActivity.class);
                intent.putExtra("userUrl", userUrl);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend selectedFriend = friendAdapter.getItem(position);
                if (selectedFriend != null) {
                    Intent intent = new Intent(SearchActivity.this, FAccountActivity.class);
                    intent.putExtra("userUrl", userUrl);
                    intent.putExtra("friendId", selectedFriend.getFriendId());
                    intent.putExtra("friendUsername", selectedFriend.getFriendUsername());
                    startActivity(intent);
                }
            }
        });

        EditText searchFriendEditText = findViewById(R.id.searchFriendEditText);
        searchFriendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFriends(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fetchAllUsers();
    }

    private void filterFriends(String query) {
        friendAdapter.clear();
        if (query.isEmpty()) {
            return;
        }

        for (String friendUsername : usernameToIdMap.keySet()) {
            if (friendUsername.toLowerCase().startsWith(query.toLowerCase())) {
                String friendId = usernameToIdMap.get(friendUsername);
                friendAdapter.add(new Friend(friendUsername, friendId));
            }
        }
        friendAdapter.notifyDataSetChanged();
    }




    private Map<String, String> usernameToIdMap = new HashMap<>();

    private void fetchAllUsers() {
        String serverUrl = "http://coms-309-021.class.las.iastate.edu:8080/player/all/names";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Set<String> uniqueUsers = new HashSet<>();
                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String username = response.optString(key);
                            uniqueUsers.add(username);
                            // Map username to a string ID (starting from 1)
                            usernameToIdMap.put(username, key);
                        }

                        // Update ArrayAdapter
                        friendAdapter.clear();
                        List<String> sortedUsernames = new ArrayList<>(uniqueUsers);
                        Collections.sort(sortedUsernames);
                        for (String username : sortedUsernames) {
                            String id = usernameToIdMap.get(username);
                            friendAdapter.add(new Friend(username, id));
                        }
                        friendAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Error fetching users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }




}
