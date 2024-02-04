package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends AppCompatActivity implements SurfaceHolder.Callback,WebSocketListener {
    private SurfaceView surfaceView;
    private PhysicsSimulator physicsSimulator;
    private LevelDataManager levelDataManager;
    private Button saveButton;
    private final int HEIGHT = 30;
    private final int WIDTH = 30;
    private static int levelNum = 0;
    private FrameLayout popupMenu;
    private Button menuButton;
    private Button friendPopupBtn;

    private boolean isMenuVisible = false; // To track if the menu is visible
    private ImageView draggableItem;
    private ImageView draggableItem2;
    private ImageView draggableItem3;

    private boolean itemIsBeingDragged = false; // To track if the item is being dragged
    private int bombCount = 2;
    private int timestwoCount = 2;
    private int reverseCount = 2;
    private PopupWindow popupWindow;
    private Animation fadeIn, fadeOut;
    private boolean isPopupVisible = false;
    List<String> friendsList;
    private Context context = this;
    private String session_id = MainActivity.getID();
    private static boolean isMultiplayer = false;



    private MyBackgroundService myBackgroundService;
    private boolean isBound = false;





    public LevelScreen(JSONObject load) {
       handleWorldDataResponse(load);

   }
   public LevelScreen(){

   }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           // MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder) service;
            //myBackgroundService = binder.getService();
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
      //  Intent intent = new Intent(this, MyBackgroundService.class);
       // bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);




        physicsSimulator = new PhysicsSimulator(this);
        levelDataManager = new LevelDataManager(physicsSimulator);

        physicsSimulator.setPlaying(true);

        //IntentFilter filter = new IntentFilter(MyBackgroundService.ACTION_WEBSOCKET_MESSAGE);
        //registerReceiver(websocketMessageReceiver, filter);

        Intent intent = getIntent(); // Get the intent that started this Activity
        if (intent != null) {
            if(intent.hasExtra("world-data")){
                    loadWorld();

            }  if (intent.hasExtra("selectedLevel")){

                int selectedLevel = getIntent().getIntExtra("selectedLevel", 0);
                getLevelData(String.valueOf(selectedLevel));
                System.out.println("Selected Level: " + selectedLevel);
                PostSimulationData(HEIGHT, WIDTH, selectedLevel,true);
                levelNum = selectedLevel;



            }

            if(intent.hasExtra("userId")){
                try {
                    // Connect to the WebSocket for LevelEditor screen
                    isMultiplayer = true;
                    session_id = intent.getStringExtra("userId");
                    String serverUrL = "ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/" + session_id+"/" + MainActivity.getID();
                    WebSocketManager.getInstance().connectWebSocket(serverUrL);
                    WebSocketManager.getInstance().setWebSocketListener(this);
                    physicsSimulator.setPlaying(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                session_id = MainActivity.getID();
            }
        }


        //JSONObject load = new JSONObject();
        //handleWorldDataResponse(load);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);




        //onclick listener for save button
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSimulationData(HEIGHT, WIDTH);
            }
        });
       Button backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSimulationData(HEIGHT, WIDTH);
                Intent intent = new Intent(LevelScreen.this, LevelList.class);
                startActivity(intent);
            }
        });

        // Inflate the menu layout
        // Find the menu layout in your XML layout
        popupMenu = findViewById(R.id.itemMenu);
        menuButton = findViewById(R.id.menuButton);



        //System.out.println("Popup Menu Handler: ");
        friendPopupBtn = findViewById(R.id.popupMenu);



        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePopupMenu();
            }
        });

        popupWindow = new PopupWindow(this);
        fetchFriends(new LevelList.Callback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                // Handle the response here
                //System.out.println("Received levels: " + response);

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



        getItemData();


        Button openPopupButton = findViewById(R.id.popupMenu);
        openPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePopup(v);
            }
        });

        draggableItem = findViewById(R.id.draggableItem);
        draggableItem2 = findViewById(R.id.draggableItem2);
        draggableItem3 = findViewById(R.id.draggableItem3);

        // Set up an OnTouchListener for the draggable item
        draggableItem2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( timestwoCount == 0){
                    Toast.makeText(LevelScreen.this, "No Times 2 left", Toast.LENGTH_SHORT).show();
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Handle the click down event
                        Log.d("DragTest", "Item clicked down");

                        // Start the drag operation when the item is clicked down
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(null, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                        return true;
                }
                return false;
            }
        });
        draggableItem3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( reverseCount == 0){
                    Toast.makeText(LevelScreen.this, "No anti Gravity left", Toast.LENGTH_SHORT).show();
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Handle the click down event
                        Log.d("DragTest", "Item clicked down");

                        // Start the drag operation when the item is clicked down
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(null, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                        return true;
                }
                return false;
            }
        });
        // Set up an OnTouchListener for the draggable item
        draggableItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(bombCount == 0){
                    Toast.makeText(LevelScreen.this, "No Bombs Left", Toast.LENGTH_SHORT).show();
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Handle the click down event
                        Log.d("DragTest", "Item clicked down");

                        // Start the drag operation when the item is clicked down
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(null, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                        return true;
                }
                return false;
            }
        });




        View dropTarget = findViewById(R.id.surfaceView);

        // Register a drag listener for the drop target
        dropTarget.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Drag has started
                        physicsSimulator.setEdit(true);
                        itemIsBeingDragged = true;
                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        // The drag shadow has entered the view
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        // The drag shadow is over the view
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        // The drag shadow has exited the view

                        return true;

                    case DragEvent.ACTION_DROP:


                        // The item has been dropped

                        float x = event.getX();
                        float y = event.getY();

                        String item_id = "";

                        if(event.getLocalState() == draggableItem2){
                            physicsSimulator.addCircleOnTouch(x, y,4);
                            timestwoCount--;
                            item_id = "2";

                        }
                        else if(event.getLocalState() == draggableItem){
                            physicsSimulator.removeCircleOnTouch(x, y,10,true);
                            bombCount--;
                            item_id = "1";
                        }else if(event.getLocalState() == draggableItem3){
                            physicsSimulator.addCircleOnTouch(x, y,6);
                            reverseCount--;
                            item_id = "3";
                        }
                        RequestQueue queue = Volley.newRequestQueue(context);



                        // Define the server URL for fetching level data by name
                        String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/player/"+MainActivity.getID()+"/useItem/"+item_id;

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, serverUrl, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle error, e.g., show an error message
                                    }
                                });

// Add the request to the RequestQueue
                        queue.add(request);


                        // Add the request to the queue for execution
                        queue.add(request);



                        physicsSimulator.setEdit(false);
                        itemIsBeingDragged = false;
                        //bombCount--;

                        // You can access the endingX and endingY variables elsewhere in your code
                       // Log.d("DragTest", "Bomb dropped at X: " + endingX + ", Y: " + endingY);


                        View view = (View) event.getLocalState();

                        // Handle the item drop here

                        // Show the original item again
                        view.setVisibility(View.VISIBLE);
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        // The drag operation has ended
                        if (!event.getResult()) {
                            // The drop was not successful, show the original item again
                            View draggedView = (View) event.getLocalState();
                            draggedView.setVisibility(View.VISIBLE);
                        }
                        return true;


                    default:
                        break;
                }
                return false;
            }
        });
        String serverUri = "ws://chiang04@coms-309-021.class.las.iastate.edu:8080/multiplayer/" +session_id+"/" + MainActivity.getID();
        WebSocketManager.getInstance().connectWebSocket(serverUri);
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

private void getItemData(){
    RequestQueue queue = Volley.newRequestQueue(this);



    // Define the server URL for fetching level data by name
    String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/player/"+MainActivity.getID()+"/inventory";

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, serverUrl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("Response: " + response);
                        // Extract values from the JSON object
                        bombCount = response.getInt("1");
                        timestwoCount = response.getInt("2");
                        reverseCount = response.getInt("3");

                        // Handle the extracted values as needed
                        // For example, log them
                        Log.d("Value 1", String.valueOf(bombCount));
                        Log.d("Value 2", String.valueOf(timestwoCount));
                        Log.d("Value 3", String.valueOf(reverseCount));

                        // Or use them in your application logic
                        // ...

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error, e.g., show an error message
                }
            });

// Add the request to the RequestQueue
    queue.add(request);


    // Add the request to the queue for execution
    queue.add(request);
}

    private void togglePopupMenu() {
        if (popupMenu.getVisibility() == View.VISIBLE) {
            hidePopupMenu();
        } else {
            showPopupMenu();
        }
    }

    private void showPopupMenu() {
        TranslateAnimation slideUp = new TranslateAnimation(0, 0, 1000, 0);
        slideUp.setDuration(300);
        popupMenu.startAnimation(slideUp);
        popupMenu.setVisibility(View.VISIBLE);
    }

    private void hidePopupMenu() {
        TranslateAnimation slideDown = new TranslateAnimation(0, 0, 0, 1000);
        slideDown.setDuration(300);
        popupMenu.startAnimation(slideDown);
        popupMenu.setVisibility(View.GONE);
    }




    // Inside your LevelList activity
    private void getLevelData(String levelName) {
        // Create a RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        System.out.println("Level Name: " + levelName);

        // Define the server URL for fetching level data by name
        String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapInfo/"+ levelName+"/getMapContentById";

        // Create a JSON request to the server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleWorldDataResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error, e.g., show an error message
                    }
                });

        // Add the request to the queue for execution
        queue.add(request);
    }

    private void handleRemoveAccountError(VolleyError error) {
        showError("Error: " + error);
        // Handle errors that occurred during the DELETE request
        // This code will be executed if there's a network error or other issues
    }

    private void showSuccessMessage(String message) {
        Toast.makeText(LevelScreen.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(LevelScreen.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String errorMessage) {
        System.out.println(errorMessage);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LevelScreen.this, MainActivity.class);
        startActivity(intent);
    }


    private void loadWorld() {
        // Replace with your server URL
        String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapContent/" + MainActivity.getID() + "/getMapContentByUser";

        // Initialize a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JsonObjectRequest to fetch world data from the server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                serverUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleWorldDataResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleWorldDataError(error);
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    void loadWorldFromID() {
        // Replace with your server URL
        String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapInfo/"+levelNum+"/getMapContentById";

        // Initialize a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JsonObjectRequest to fetch world data from the server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                serverUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleWorldDataResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleWorldDataError(error);
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    private void handleWorldDataResponse(JSONObject response) {
        try {
            //physicsSimulator.setDestroyWorld(true);
            // Extract and deserialize the Bodies data
            String bodiesData = response.getString("message");
           // System.out.println(bodiesData);

            physicsSimulator.updateBallGrid(LevelDataManager.deserializeListOfCircleBodies(bodiesData));
            // Handle other parts of the world data if needed
        } catch (JSONException e) {
            e.printStackTrace();
            handleWorldDataError(null);
        }
    }

    private void handleWorldDataError(VolleyError error) {
        String errorMessage = (error != null) ? "Error loading world: " + error : "Error loading world";
        Toast.makeText(LevelScreen.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
    private void PostSimulationData(int numRows, int numCols, int levelNum, boolean handleSave) {
        try {
            //System.out.println("Posting...");
            // Create a JSON object to hold the simulation data
            JSONObject requestData = new JSONObject();
            Integer[][] listOfCircleBodies =levelDataManager.serializeBodies(numRows, numCols);

            // Create a JSON array to store the body data
            JSONArray bodyArray = new JSONArray();
            StringBuilder sb = new StringBuilder();

            for (int i = 0;i< numRows;i++){
                for (int j = 0;j<numCols;j++){
                    sb.append(listOfCircleBodies[i][j]);
                }
            }


            // Add the body data to the request data
            requestData.put("body", sb.toString());

            // Instantiate the RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);

            // Define the URL where you want to send the data
           // System.out.println(levelNum);
            String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapContent/mapContent/"+MainActivity.getID()+"/"+(int)levelNum+"/save";

            // Create a POST request to send the JSON data
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (handleSave) {
                                handleSaveResponse(response);
                            }
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



    private void saveSimulationData(int numRows, int numCols) {
        try {
            System.out.println("Save Button Clicked");
            //showCustomDialog();

            // Create a JSON object to hold the simulation data
            JSONObject requestData = new JSONObject();
            Integer[][] listOfCircleBodies =LevelDataManager.serializeBodies(numRows, numCols);

            // Add the body data to the request data
            requestData.put("body", LevelDataManager.ArrToString(listOfCircleBodies));

            // Instantiate the RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);

            // Define the URL where you want to send the data
            String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapContent/"+MainActivity.getID()+"/Body/updateColumn";

            // Create a POST request to send the JSON data
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, requestData,
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


    void handleSaveResponse(JSONObject response) {
        try {
            String message = response.getString("message");
            System.out.println("Response: " + message);
            if (message.equals("this map is being played")) {
                String lable = "this map is being played";
                showCustomDialog(lable);
                showSuccessMessage("You Are currently Playing Another Level");

            } else if(message.equals("another map is being played")){
                String lable = "another map is being played";
                showCustomDialog(lable);
                showSuccessMessage("You Are currently Playing Another Level");
            }
            else {
                showSuccessMessage("Data saved");
            }
            // You can notify the user about the successful save if needed
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }
    }
    void showCustomDialog(String lable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Data");
        if(lable.equals("this map is being played")){
            builder.setMessage("You have a old save of this level. What would you like to do?");
        } else{
            builder.setMessage("You currently have another level saved. What would you like to do?");

        }

        builder.setPositiveButton("Continue Playing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapContent/"+MainActivity.getID()+"/delete";

// Create a Volley DELETE request
                StringRequest request = new StringRequest(Request.Method.DELETE, serverUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the success response here
                                System.out.println("Response: " + response);
                                PostSimulationData(HEIGHT, WIDTH,levelNum,false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error response here
                                handleRemoveAccountError(error);
                            }
                        }
                );

// Add the request to the RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);


                // Add the request to the Volley queue for execution

               // PostSimulationData(HEIGHT, WIDTH,levelNum);
                // Handle the "Continue Playing" button click
                // You can add your logic to continue playing here
            }
        });

        builder.setNegativeButton("Load Another Level", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadWorld();
                // Handle the "Load Another Level" button click
                // You can add your logic to load another level here
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void handleSaveError(VolleyError error) {
        String errorMessage = (error != null) ? "Error saving data: " + error : "Error saving data";
        System.out.println(errorMessage);
        // You can notify the user about the error as needed
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
        if(!itemIsBeingDragged) {
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    // Handle a touch event
                    physicsSimulator.removeCircleOnTouch(x, y, 2,false);
                    break;
                // Handle other touch actions as needed
            }
        }

        return true;
    }
    public static int getLevelNum(){
        return levelNum;
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
                            int levelId = getLevelNum();
                            System.out.println("level id: "+levelId);


                            myBackgroundService.sendNotification(MainActivity.getID(),friend,"play",String.valueOf(levelId));

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
    private List<String> parseResponse(JSONArray response) throws JSONException {
        List<String> friendNames = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            friendNames.add(response.getString(i));
        }
        return friendNames;
    }


    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket Connected");

    }
    boolean ishostjoin = false;

    @Override
    public void onWebSocketMessage(String message) {
        System.out.println("WebSocket Message received: " + message);

        // Assuming you have a TextView with the id "messageTextView" in your layout


        if (message.contains("joined") && session_id.equals(MainActivity.getID())) {
            if (!ishostjoin) {
                ishostjoin = true;
                return;
            }
            physicsSimulator.setPlaying(false);
            isMultiplayer = true;

            // Append the message to the TextView


            // Run UI-related code on the main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView messageTextView = findViewById(R.id.messageTextView);
                    String currentText = messageTextView.getText().toString();
                    String newText = currentText + "\n" + message;
                    messageTextView.setText(newText);
                    // Create a button and set its properties
                    Button startButton = new Button(context);
                    startButton.setText("Start");

                    // Set layout parameters to center the button
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.gravity = Gravity.CENTER;
                    startButton.setLayoutParams(layoutParams);

                    // Set an onClickListener for the button
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Remove the button
                            startButton.setVisibility(View.GONE);
                            WebSocketManager.getInstance().sendMessage("start");
                            messageTextView.setText("");
                        }
                    });

                    // Add the button to the root layout of your activity
                    ViewGroup rootView = findViewById(android.R.id.content);
                    rootView.addView(startButton);
                }
            });


        }if(message.contains("start")){
            System.out.println("countdown started");
            displayCountdown();


        }
        if(message.contains("win")){
            //physicsSimulator.setPlaying(false);
            String[] parts = message.split(",");
            String winner = parts[1];
            if(winner.equals(MainActivity.getID())) {
                System.out.println("you win");
                physicsSimulator.showWinPopup(true);
            }else{
                System.out.println("you lose");
                physicsSimulator.showWinPopup(false);

            }


        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket Closed");
    }

    @Override
    public void onWebSocketError(Exception ex) {
        System.out.println("WebSocket Error: " + ex.getMessage());
    }

    @Override
    public void onWebSocketConnectionSuccess() {

    }

    @Override
    public void onWebSocketConnectionFailure(Exception ex) {

    }


    private void displayCountdown() {
        // Run UI-related code on the main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Create a TextView for displaying the countdown
                final TextView countdownTextView = new TextView(context);
                countdownTextView.setTextSize(50);
                countdownTextView.setTextColor(Color.WHITE);

                // Set layout parameters to center the TextView
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.gravity = Gravity.CENTER;
                countdownTextView.setLayoutParams(layoutParams);

                // Add the TextView to the root layout of your activity
                ViewGroup rootView = findViewById(android.R.id.content);
                rootView.addView(countdownTextView);

                // Start a 3-second countdown
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Update the TextView with the remaining time
                        countdownTextView.setText("Countdown: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        // Remove the TextView when the countdown is finished
                        rootView.removeView(countdownTextView);


                        System.out.println("countdown finished");




                        loadWorldFromID();


                        physicsSimulator.resetscore();

                        physicsSimulator.loadballs();

                        physicsSimulator.setPlaying(true);

                    }
                }.start();
            }
        });
    }

    public static void finnished(){
        //System.out.println("you win");
        try {
            WebSocketManager.getInstance().sendMessage("win,"+MainActivity.getID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isMultiplayer(){
        return isMultiplayer;
    }
}
