
package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class LevelScreen extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private PhysicsSimulator physicsSimulator;
    private LevelDataManager levelDataManager;
    private Button saveButton;
    private final int HEIGHT = 30;
    private final int WIDTH = 30;
    private static int levelNum = 0;
    private FrameLayout popupMenu;
    private Button menuButton;
    private boolean isMenuVisible = false; // To track if the menu is visible
    private ImageView draggableItem;
    private boolean itemIsBeingDragged = false; // To track if the item is being dragged
    private int bombCount = 2;




    public LevelScreen(JSONObject load) {
       handleWorldDataResponse(load);

   }
   public LevelScreen(){

   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        physicsSimulator = new PhysicsSimulator(this);
        levelDataManager = new LevelDataManager(physicsSimulator);

        physicsSimulator.setPlaying(true);

        Intent intent = getIntent(); // Get the intent that started this Activity
        if (intent != null) {
            if(intent.hasExtra("world-data")){
                    loadWorld();

            }  if (intent.hasExtra("selectedLevel")){

                int selectedLevel = getIntent().getIntExtra("selectedLevel", 0);
                getLevelData(String.valueOf(selectedLevel));
                System.out.println("Selected Level: " + selectedLevel);
                PostSimulationData(HEIGHT, WIDTH, selectedLevel);
                levelNum = selectedLevel;

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


        // Inflate the menu layout
        // Find the menu layout in your XML layout
        popupMenu = findViewById(R.id.popupMenu);
        menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePopupMenu();
            }
        });

        draggableItem = findViewById(R.id.draggableItem);


        // Set up an OnClickListener for the draggable item
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




        // Set up the drop target for the screen (where you want to drop the item)
        View dropTarget = findViewById(R.id.surfaceView); // Replace with your actual drop target view

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



                        physicsSimulator.removeCircleOnTouch(x, y,10,true);

                        physicsSimulator.setEdit(false);
                        itemIsBeingDragged = false;
                        bombCount--;

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

    private void removeAccount() {
        String playerId = MainActivity.getID();
        String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/account/deleteAccount/" + playerId;

        // Create a Volley DELETE request
        StringRequest request = new StringRequest(Request.Method.DELETE, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleRemoveAccountResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleRemoveAccountError(error);
                    }
                });

        // Add the request to the Volley queue for execution
        Volley.newRequestQueue(this).add(request);
    }

    private void handleRemoveAccountResponse(String response) {
        if (response.equals("true")) {
            showSuccessMessage("Account Deleted");
            navigateToMainActivity();
        } else {
            showErrorMessage("Account Not Deleted");
        }
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

    private void handleWorldDataResponse(JSONObject response) {
        try {
            //physicsSimulator.setDestroyWorld(true);
            // Extract and deserialize the Bodies data
            String bodiesData = response.getString("body");
            System.out.println(bodiesData);

            physicsSimulator.updateBallGrid(LevelDataManager.deserializeListOfCircleBodies(bodiesData));
//            Integer[][] grid = new Integer[HEIGHT][WIDTH];
//            for (int i = 0; i < HEIGHT; i++) {
//                for (int j = 0;j<WIDTH;j++){
//                    grid[i][j] = 2;
//                }
//            }
//            physicsSimulator.updateBallGrid(grid);

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
    private void PostSimulationData(int numRows, int numCols, int levelNum) {
        try {
            System.out.println("Posting...");
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
            System.out.println(levelNum);
            String url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapContent/mapContent/"+MainActivity.getID()+"/"+(int)levelNum+"/save";

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


    private void handleSaveResponse(JSONObject response) {
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
    private void showCustomDialog(String lable) {
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

                PostSimulationData(HEIGHT, WIDTH,levelNum);
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


}
