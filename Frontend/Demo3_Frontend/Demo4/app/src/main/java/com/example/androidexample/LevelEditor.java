package com.example.androidexample;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LevelEditor extends AppCompatActivity implements SurfaceHolder.Callback, WebSocketListener {
    private WebSocketClient webSocketClient;
    private SurfaceView surfaceView;
    private LevelDataManager levelDataManager;
    private PhysicsSimulator physicsSimulator;
    private  final int HEIGHT = 30;
    private  final int WIDTH = 30;
    private PopupWindow popupWindow;
    private Animation fadeIn, fadeOut;
    private boolean isPopupVisible = false;
    List<String> friendsList;
    private Context context = this;
    private String session_id = "4";


    private MyBackgroundService myBackgroundService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder) service;
            myBackgroundService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
        Intent intent = new Intent(this, MyBackgroundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_level_editor);
        System.out.println("Level Editor");

        IntentFilter filter = new IntentFilter(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE);
        registerReceiver(websocketMessageReceiver, filter);



        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        physicsSimulator = new PhysicsSimulator(this);
        physicsSimulator.setEdit(true);

        levelDataManager = new LevelDataManager(physicsSimulator);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            if (receivedIntent.getStringExtra("userId") != null) {
                String userId = receivedIntent.getStringExtra("userId");
                session_id = userId;
                System.out.println("session_id" + session_id);
            } else {
                session_id = MainActivity.getID();
            }
        }




        Button PostLevel = findViewById(R.id.post);
        PostLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSimulationData(HEIGHT, WIDTH);
            }
        });
        Button exitbtn = findViewById(R.id.quit);
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelEditor.this, MainMenu.class);
                startActivity(intent);
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

        popupWindow = new PopupWindow(this);
        fetchFriends(new LevelList.Callback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                // Handle the response here
                System.out.println("Received levels: " + response);

                popupWindow.setContentView(getPopUpView(response));
                popupWindow.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);

                // Initialize animations
                fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);


            }

            @Override
            public void onError(VolleyError error) {
                // Handle the error here
                System.out.println("Error fetching levels from the server");
            }
        });






        Button openPopupButton = findViewById(R.id.popupMenu);
        openPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePopup(v);
            }
        });
        Button undoButton = findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });

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
        Button magenta = findViewById(R.id.magenta);
        magenta.setOnClickListener(commonClickListener);







        try {
            if(session_id.equals(MainActivity.getID())){
                MakeEditTable();
            }
            String serverUri = ("ws://chiang04@coms-309-021.class.las.iastate.edu:8080/editing/"+ session_id +"/"+MainActivity.getID());
           // String serverUrl = serverUri + MainActivity.getID();
            System.out.println(serverUri);
            WebSocketManager.getInstance().connectWebSocket(serverUri);
            WebSocketManager.getInstance().setWebSocketListener(LevelEditor.this);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
private void MakeEditTable(){
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/EditingMap/newMap/post/"+session_id;

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                }

            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error",error.toString());

                }
            }
    );

    queue.add(request);
}
    private void CloseEditTable(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/EditingMap/deleteAll/"+session_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.toString());

                    }
                }
        );

        queue.add(request);
    }
private void undo() {
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/EditingMap/undoChanges/"+session_id;

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try {
                        String message = response.getString("message");
                        if(message.equals("null")){
                            return;
                        }
                        physicsSimulator.updateBallGrid(LevelDataManager.deserializeListOfCircleBodies(message));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("undo!");
//                    try {
//
//                        //physicsSimulator.updateBallGrid(LevelDataManager.deserializeListOfCircleBodies(response.toString()));
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }


                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error",error.toString());
                    System.out.println("error undo!");

                }
            }
    );

    queue.add(request);
}

    private void togglePopup(View anchorView) {
        if (isPopupVisible) {
            // Apply fadeOut animation
            popupWindow.getContentView().startAnimation(fadeOut);
            popupWindow.dismiss();
        } else {
            int[] location = new int[2];
            anchorView.getLocationOnScreen(location);

            // Adjust the x and y coordinates to position the popup on the right side
            int x = location[0] + anchorView.getWidth();
            int y = location[1];

            // Apply fadeIn animation
            popupWindow.getContentView().startAnimation(fadeIn);

            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
        }

        // Toggle the visibility flag
        isPopupVisible = !isPopupVisible;
    }

    private View getPopUpView(List<String> friendsList) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.popup_background); // Add a custom background

        // Add padding to the popup
        int paddingInPixels = 8;
        linearLayout.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

        // Create a ColorStateList for text color
        ColorStateList textColorStateList = getResources().getColorStateList(R.color.button_text_color_selector);

        for (String friend : friendsList) {
            Button friendButton = new Button(this);
            friendButton.setText(friend);
            friendButton.setTextColor(textColorStateList);
            friendButton.setBackgroundResource(R.drawable.button_selector); // Use the button state selector
            friendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reset the background of all buttons
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        View childView = linearLayout.getChildAt(i);
                        if (childView instanceof Button) {
                            childView.setBackgroundResource(R.drawable.button_selector);

                            myBackgroundService.sendNotification(MainActivity.getID(),friend, "edit", "1");

                        }
                    }

                    // Change the background of the clicked button to indicate selection
                    friendButton.setBackgroundResource(R.drawable.button_normal);

                    // Handle button click for each friend
                    // You can add your logic here
                    togglePopup(v);
                }
            });
            linearLayout.addView(friendButton);
        }

        return linearLayout;
    }


    private BroadcastReceiver websocketMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE)) {
                String message = intent.getStringExtra(MyBackgroundService.EXTRA_WEBSOCKET_MESSAGE);
                // Display the message on your UI
                System.out.println("Received message: " + message);

                Drawnotification.showGameInvitationDialog(context, message);

            }
        }
    };


    private void saveSimulationData(int numRows, int numCols) {
        try {
            //showCustomDialog();
            CloseEditTable();

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
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);

            // You can notify the user about the successful save if needed
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }
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

            case MotionEvent.ACTION_UP:
                // Handle a touch event
                System.out.println("Touch event: " + x + ", " + y);
                sendLevelDataToServer(LevelDataManager.serializeBodies(HEIGHT, WIDTH));
                break;
            case MotionEvent.ACTION_MOVE:
                physicsSimulator.removeCircleOnTouch(x,y,2,false);

            case MotionEvent.ACTION_DOWN:
                physicsSimulator.removeCircleOnTouch(x,y,2,false);
                break;


            // Handle other touch actions as needed
        }

        return true;
    }

    private void fetchFriends(final LevelList.Callback<List<String>> callback) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/player/"+MainActivity.getID()+"/friends/names";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> friendNames = null;
                        try {
                            friendNames = parseResponse(response);
                            friendsList = friendNames;
                            System.out.println(friendNames.size());
                            callback.onResponse(friendNames);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        for (String friendName : friendNames) {
                            Log.d("Friend Name", friendName);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Log.d("error",error.toString());
                        callback.onError(error);

                    }
                }
        );

        queue.add(request);
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

                    try {
                        System.out.println(message);
                        physicsSimulator.updateBallGrid(LevelDataManager.deserializeListOfCircleBodies(message));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }






        //}
//);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        System.out.println("WebSocket connection closed by " + closedBy + "\nreason: " + reason);
        // String s = msgTv.getText().toString();
        //msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connection opened");
    }

    @Override
    public void onWebSocketError(Exception ex) {}

    @Override
    public void onWebSocketConnectionSuccess() {

    }

    @Override
    public void onWebSocketConnectionFailure(Exception ex) {

    }


    // Parse the JSON response and extract friend names
    private List<String> parseResponse(JSONArray response) throws JSONException {
        List<String> friendNames = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            friendNames.add(response.getString(i));
        }
        return friendNames;
    }
}


