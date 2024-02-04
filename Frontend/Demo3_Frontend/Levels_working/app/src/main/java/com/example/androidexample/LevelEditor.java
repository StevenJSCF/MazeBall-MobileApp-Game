package com.example.androidexample;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class LevelEditor extends AppCompatActivity implements SurfaceHolder.Callback, WebSocketListener {
    private WebSocketClient webSocketClient;
    private SurfaceView surfaceView;
    private LevelDataManager levelDataManager;
    private PhysicsSimulator physicsSimulator;
    private  final int HEIGHT = 30;
    private  final int WIDTH = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_editor);
        System.out.println("Level Editor");

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        physicsSimulator = new PhysicsSimulator(this);
        physicsSimulator.setEdit(true);

        levelDataManager = new LevelDataManager(physicsSimulator);

        Button PostLevel = findViewById(R.id.post);
        PostLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSimulationData(HEIGHT, WIDTH);
            }
        });


        View.OnClickListener commonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the tag associated with the clicked button
                Object tag = view.getTag();
                if (tag != null) {
                    int tagValue = Integer.parseInt(tag.toString());
                    // Now 'tagValue' contains the tag of the clicked button
                    physicsSimulator.setEditNum(tagValue);
                }
            }
        };

        Button remove = findViewById(R.id.remove);
        remove.setOnClickListener(commonClickListener);
        Button red = findViewById(R.id.red);
        red.setOnClickListener(commonClickListener);
        Button blue = findViewById(R.id.blue);
        blue.setOnClickListener(commonClickListener);
        Button yellow = findViewById(R.id.yellow);
        yellow.setOnClickListener(commonClickListener);
        Button green = findViewById(R.id.green);
        green.setOnClickListener(commonClickListener);


        try {
            String serverUri = ("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/editing/");


            String serverUrl = serverUri + MainActivity.getID();
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(LevelEditor.this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSimulationData(int numRows, int numCols) {
        try {
            //showCustomDialog();

            // Create a JSON object to hold the simulation data
            JSONObject requestData = new JSONObject();
            Integer[][] listOfCircleBodies =LevelDataManager.serializeBodies(numRows, numCols);

            // Add the body data to the request data
            requestData.put("body", LevelDataManager.ArrToString(listOfCircleBodies));

            // Instantiate the RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);

            // Define the URL where you want to send the data
            String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapInfo/postMap/"+ MainActivity.getID();

            // Create a POST request to send the JSON data
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleSaveResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleSaveError(error);
                        }
                    });

            // Add the request to the RequestQueue
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON creation error
        }
    }

    private void handleSaveError(VolleyError error) {
        String errorMessage = (error != null) ? "Error saving data: " + error : "Error saving data";
        System.out.println(errorMessage);
        // You can notify the user about the error as needed
    }


    private void handleSaveResponse(JSONObject response) {
        try {
            String message = response.getString("message");
            System.out.println("Response: " + message);

            // You can notify the user about the successful save if needed
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }
    }

    private void showSuccessMessage(String message) {
        Toast.makeText(LevelEditor.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(LevelEditor.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String errorMessage) {
        System.out.println(errorMessage);
    }


    public void sendLevelDataToServer(Integer[][] levelData) {
        int numRows = HEIGHT;
        int numCols = WIDTH;


            StringBuilder sb = new StringBuilder();

            for (int i = 0;i< numRows;i++){
                for (int j = 0;j<numCols;j++){
                    sb.append(levelData[i][j]);
                }
            }


            WebSocketManager.getInstance().sendMessage(sb.toString());

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start a thread to update the physics simulation and draw objects
        PhysicsThread physicsThread = new PhysicsThread(surfaceView.getHolder(), physicsSimulator);
        physicsThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Not needed for this example
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Cleanup resources if needed
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                // Handle a touch event
                physicsSimulator.removeCircleOnTouch(x,y,2,false);
                sendLevelDataToServer(LevelDataManager.serializeBodies(HEIGHT, WIDTH));
                break;
            // Handle other touch actions as needed
        }

        return true;
    }
    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        //runOnUiThread(() -> {
        System.out.println(message);
        // Split the message into words
        String[] words = message.split("\\s+");

// Check if the first word is "Welcome" and if there are at least two words in the message
        if (words.length >= 2 && words[0].equals("Welcome")) {
            // Attempt to parse the second word (first character after "Welcome") as an integer
            try {
                int firstCharacter = Integer.parseInt(words[1].substring(0, 1));
                // Check if it's an integer before running the physicsSimulator code
                if (firstCharacter >= 0 && firstCharacter <= 9) {
                    // The first character is an integer
                    try {
                        System.out.println(message);
                        physicsSimulator.updateBallGrid(LevelDataManager.deserializeListOfCircleBodies(message));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (NumberFormatException e) {
                // The second word after "Welcome" is not an integer
                // You can handle this case if needed
            }
        }






        //}
//);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";

           // String s = msgTv.getText().toString();
            //msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connection opened");
    }

    @Override
    public void onWebSocketError(Exception ex) {}



}

