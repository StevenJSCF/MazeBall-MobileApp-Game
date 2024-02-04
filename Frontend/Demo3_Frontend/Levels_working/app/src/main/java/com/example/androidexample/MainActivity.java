package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private final String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/account/login1"; // Replace with your JSON server URL
private static String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Create Account" button click
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent intent = new Intent(MainActivity.this, MainMenu.class);
               // startActivity(intent);
                // Get username and password from EditText fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Create a JSON object with login credentials
                JSONObject credentials = new JSONObject();
                try {
                    credentials.put("username", username);
                    credentials.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send a Volley POST request to your JSON server
                System.out.println("Sending request");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverUrl, credentials,
                        new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {

                                //System.out.println("Response: " + response.toString());
                                Log.d("Response", response.toString());
                                // Handle the server response here
                                try {
                                    String message = response.getString("message");
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                     ID  = String.valueOf(Integer.parseInt(message));

                                     if (ID.equals(-1)){
                                        // Intent to your next activity
                                       // message = response.getString("ID");
                                       // ID = message;
                                        System.out.println("Login Failed");

                                    }else {
                                        Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                        startActivity(intent);
                                        System.out.println("Login Successful");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response here
                                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println("Error: " + error.getMessage());
                            }
                        });

                // Add the request to the Volley request queue
                Volley.newRequestQueue(MainActivity.this).add(request);
            }


        });



    }
    public static String getID(){
        return ID;
    }


}
