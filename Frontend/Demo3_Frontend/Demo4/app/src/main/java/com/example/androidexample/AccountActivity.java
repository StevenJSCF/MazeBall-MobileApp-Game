package com.example.androidexample;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
    ImageView imageView;

    private String userId;
  Button changeButton;


    @Override
        protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        messageText = findViewById(R.id.account_msg_txt);
        messageText.setText("Profile");


        // Initialize the spinner
        Spinner spinner = findViewById(R.id.sp_activity);

        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.spinner_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);



        String userUrl = getIntent().getStringExtra("userUrl");
        String[] parts = userUrl.split("/");
        userId = parts[parts.length - 1];


        int imageId = getIntent().getIntExtra("imageId", -1);

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




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Active")) {
                    // Handle "Active" selection
                    changeStatus(true);
                } else if (selectedItem.equals("Invisible")) {
                    // Handle "Invisible" selection
                    changeStatus(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another callback interface.
            }
        });

        changeButton = findViewById(R.id.Change_button);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        fetchImageId();
        fetchUserData();
        fetchFriendUsername();

    }

    private void changeStatus(Boolean flag){
        String url = "";
        if(flag){
            url = "http://coms-309-021.class.las.iastate.edu:8080/active/" + userId + "/active/" + userId ;
        }else{
            url = "http://coms-309-021.class.las.iastate.edu:8080/doNotDisturb/" + userId ;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                response -> {

                    Toast.makeText(this, "Change status success", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(this, "Error fetching username", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    private void fetchFriendUsername() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/name";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    updateUsernameUI(response.trim());
                },
                error -> Toast.makeText(this, "Error fetching username", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }


    private void updateUsernameUI(String username) {
        TextView usernameTextView = findViewById(R.id.username_view); // Replace with your actual TextView ID
        usernameTextView.setText("Username: " + username);
    }




    /**
     * Use the GET to request a string response from the provided URL.
     */
    private void fetchUserData() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String userId = jsonResponse.getString("id");
                        JSONObject inventory = jsonResponse.getJSONObject("inventory");
                        int money = jsonResponse.optInt("money", 0); // Default to 0 if not present

                        // Update the UI with the fetched data
                        updateUI(userId, inventory, money);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    private void updateUI(String userId, JSONObject inventory, int money) {
        TextView userIdTextView = findViewById(R.id.userid_view);
        userIdTextView.setText("User ID: " + userId);

        TextView item1TextView = findViewById(R.id.Item1_view);
        TextView item2TextView = findViewById(R.id.item2_view);
        TextView item3TextView = findViewById(R.id.item3_view);

        item1TextView.setText(inventory.optString("1", "No item"));
        item2TextView.setText(inventory.optString("2", "No item"));
        item3TextView.setText(inventory.optString("3", "No item"));

        TextView moneyTextView = findViewById(R.id.money_view);
        moneyTextView.setText("Money: " + money);
    }





    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        // Text
        TextView textView = dialogView.findViewById(R.id.textViewDialog);


        // Buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Handle the OK button
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });


        final AlertDialog alertDialog = builder.create();

        //Image
        ImageView icon1 = dialogView.findViewById(R.id.icon1);
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImageId(1); // Assuming 1 is the ID for icon1
                alertDialog.dismiss(); // Close the dialog after selection
            }
        });


        ImageView icon2 = dialogView.findViewById(R.id.icon2);
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImageId(2); // Assuming 1 is the ID for icon1
                alertDialog.dismiss(); // Close the dialog after selection
            }
        });


        ImageView icon3 = dialogView.findViewById(R.id.icon3);
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImageId(3); // Assuming 1 is the ID for icon1
                alertDialog.dismiss(); // Close the dialog after selection
            }
        });


        ImageView icon4 = dialogView.findViewById(R.id.icon4);
        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImageId(4); // Assuming 1 is the ID for icon1
                alertDialog.dismiss(); // Close the dialog after selection
            }
        });

        ImageView icon5 = dialogView.findViewById(R.id.icon5);
        icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImageId(5); // Assuming 1 is the ID for icon1
                alertDialog.dismiss(); // Close the dialog after selection
            }
        });




        alertDialog.show();
    }

    private void postImageId(int imageId) {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/image/" + imageId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    Toast.makeText(AccountActivity.this, "Image ID updated", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(AccountActivity.this, "Error in updating image ID", Toast.LENGTH_SHORT).show();
                });

        queue.add(stringRequest);
    }


    private void fetchImageId() {
        String url = "http://coms-309-021.class.las.iastate.edu:8080/player/" + userId + "/image";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    int imageId = Integer.parseInt(response.trim());
                    updateImageView(imageId);
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error fetching image ID", Toast.LENGTH_SHORT).show();
                });

        queue.add(stringRequest);
    }



    private void updateImageView(int imageId) {
        ImageView Photo = findViewById(R.id.image_picture);
        switch (imageId) {
            case 1:
                Photo.setImageResource(R.drawable.icon1);
                break;
            case 2:
                Photo.setImageResource(R.drawable.icon2);
                break;

            case 3:
                Photo.setImageResource(R.drawable.icon3);
                break;

            case 4:
                Photo.setImageResource(R.drawable.icon4);
                break;

            default:
                Photo.setImageResource(R.drawable.icon5); // Default or placeholder image
                break;
        }
    }



}
