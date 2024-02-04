package com.example.sumon.androidvolley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private String email, password;
    private String URL = "http://coms-309-021.class.las.iastate.edu:8080/account/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = password = "";
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }



// ...

    public void login(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (!email.equals("") && !password.equals("")) {
            // Create a JSON object to hold the username and password
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("username", email);
                jsonBody.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Define the URL for your login API
            String loginUrl = "https://079fe7eb-4ff2-41d4-ba77-945c8a910097.mock.pstmn.io/login";

            // Create a RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            System.out.println("Request: " + jsonBody.toString());




            // Create a JsonObjectRequest to send the login request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST,
                    loginUrl,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Response: " + response.toString());
                            try {
                                boolean success = response.getBoolean("success");

                                if (success) {
                                    // Login successful, handle the response here
                                    System.out.println("Login successful");
                                    // You can start a new activity or perform other actions
                                } else {
                                    System.out.println("Login failed");
                                    // Login failed, show an error message or handle it accordingly
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error responses here
                            System.out.println("Error: " + error.toString());
                        }
                    }

            );
            System.out.println("Request: " + jsonObjectRequest.toString());


            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        } else {
            // Handle empty email or password fields
            System.out.println("Empty email or password fields");
        }
    }


}