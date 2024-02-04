package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNewUsername, editTextNewPassword, editTextEmail;
    private Button buttonRegister;
    private String serverUrl = ""; // Replace with your registration API endpoint URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNewUsername = findViewById(R.id.editTextNewUsername);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user registration data from EditText fields
                String newUsername = editTextNewUsername.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String email = editTextEmail.getText().toString();

                // Create a JSON object with user registration data
                JSONObject userData = new JSONObject();
                try {
                    userData.put("username", newUsername);
                    userData.put("password", newPassword);
                    userData.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send a Volley POST request to your registration API
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverUrl, userData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the server response here
                                try {
                                    String message = response.getString("message");
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                                    // Check if registration is successful
                                    if (response.getBoolean("success")) {
                                        // Registration successful, you can navigate to another activity
                                        // For example, you can navigate back to the login screen
                                        finish(); // Finish the registration activity
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
                                Toast.makeText(RegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // Add the request to the Volley request queue
                Volley.newRequestQueue(RegisterActivity.this).add(request);
            }
        });
    }
}
