package com.example.androidexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {
    private World world;
    private List<Body> bodies;
    private Ball[][] listOfCircleBodies;
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    private Boolean isEdit = false;
    private int editNum = 0;
    private ArrayList<Body> gameBalls = new ArrayList<>();
    private boolean playing = false;
    private boolean won = false;
    private boolean destroyWorld = false;
    private Context context;
    private ParticleSystem particleSystem;
    private long startTime;



    public PhysicsSimulator(Context contex) {
        this.context = contex;
        // Create a new physics world with gravity
        world = new World(new Vec2(0f, 10)); // Gravity in m/s^2, adjusted for the direction
        bodies = new ArrayList<>();
        listOfCircleBodies = new Ball[WIDTH][HEIGHT];
        createBallGrid(0.5f, 2, WIDTH, HEIGHT, 0.001f);
        startTime = System.currentTimeMillis();

       // createBalls(5, 0);
        //createBalls(4.5f, -10);
    }

    public void createBallGrid(float x, float y, int width, int height, float radius) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Determine the number based on the row or column index
                //int number = (i < width / 2) ? 2 : 1;
                Ball ball = createBall(x + i * radius * 350, y + j * radius * 350, 1, BodyType.STATIC);
                listOfCircleBodies[i][j] = ball;
            }
        }
    }

    private Integer[][] BalltoInt(Ball[][] Ballsm) {
        Integer[][] intArray = new Integer[Ballsm.length][Ballsm[0].length];
        for (int i = 0; i < Ballsm.length; i++) {
            for (int j = 0; j < Ballsm[i].length; j++) {
                intArray[i][j] = Ballsm[i][j].getNumber();
            }
        }


        return intArray;
    }

    public void updateBallGrid(Integer[][] newGrid) {
        Integer[][] previousGrid = BalltoInt(listOfCircleBodies);
        int numRows = newGrid.length;
        int numCols = newGrid[0].length;


        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int newValue = newGrid[i][j];
                if (newValue != previousGrid[i][j]) {
                    // Value in the grid has changed, update the corresponding Ball
                    listOfCircleBodies[i][j].setNumber(newValue);
                    previousGrid[i][j] = newValue; // Update the previous grid state
                }
            }
        }
    }

    public void addCircleOnTouch(float x, float y,int num) {
        // First, remove the current Ball from the world
        editNum = num;
        removeCircleOnTouch(x, y, 5, false);
        editNum = 0;
        //createBalls(x, y);
    }
    public void removeCircleOnTouch(float touchX, float touchY, int radius, boolean particals) {
        try {
            Vec2 touchPoint = new Vec2(pixelsToMeters(touchX), pixelsToMeters(touchY));

            if (listOfCircleBodies.length != 0) {
                for (int i = 0; i < listOfCircleBodies.length; i++) {
                    for (int j = 0; j < listOfCircleBodies[i].length; j++) {
                        Ball circleBody = listOfCircleBodies[i][j];

                        // Check if the circleBody is not null and has fixtures
                        if (circleBody != null && circleBody.getFixtureList() != null) {
                            CircleShape circleShape = (CircleShape) circleBody.getFixtureList().getShape();
                            Vec2 circleCenter = circleBody.getWorldPoint(circleShape.m_p);
                            float distance = touchPoint.sub(circleCenter).length();

                            if (distance < circleShape.m_radius * radius) {
                                if (!isEdit) {
                                    if (listOfCircleBodies[i][j].getNumber() == 1) {
                                        listOfCircleBodies[i][j].setNumber(0);
                                    }
                                } else {
                                   // System.out.println("EditNum: " + editNum);
                                    listOfCircleBodies[i][j].setNumber(editNum);
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("listOfCircleBodies is null or empty");
            }

            if(particals){
                 particleSystem = new ParticleSystem(world, 10.0f);

// Create and add particles when needed
                Vec2 initialPosition = new Vec2(touchX, touchY); // Set the initial position
                Vec2 initialVelocity = new Vec2(1, 100); // Set the initial velocity
                float particleRadius = .1f; // Set the particle radius
                Vec2 posMeters = new Vec2(pixelsToMeters(touchX), pixelsToMeters(touchY)) ;


                particleSystem.createParticle(posMeters, initialVelocity, particleRadius);


// Within your game loop, update the particle system
                float timeStep = 1.0f / 60.0f; // Adjust as needed
                particleSystem.update(timeStep);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    float pixelsToMeters(float pixels) {
        float pixelsToMeters = 1.0f / 100;
        return pixels * pixelsToMeters;
    }

    public Ball createBall(float x, float y, int number, BodyType bt) {

        float pixelsToMeters = 1.0f / 100;
        float pixelRadius = 20.0f;
        float meterRadius = pixelRadius * pixelsToMeters;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bt;

        bodyDef.position.set(x, y);

        world.createBody(bodyDef);

        // Create a new Ball object with the provided parameters
        Ball ball = new Ball(bodyDef, world, number, bt);

        // You should NOT create the fixture in this method.
        // fixtureDef and the shape should be added when initializing the Ball object.

        ball.setGravityScale(1.0f);

        return ball;
    }


    public Body createBalls(float x, float y) {
        float pixelsToMeters = 1.0f / 100;
        float pixelRadius = 20.0f;
        float meterRadius = pixelRadius * pixelsToMeters;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

        Body ballBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(meterRadius);
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        ballBody.setGravityScale(1.0f);

        ballBody.createFixture(fixtureDef);

        bodies.add(ballBody);

        return ballBody;
    }

    public void update(float elapsedTime) {
        world.step(elapsedTime, 6, 2);

        // In your game loop or update method
        if(playing) {
            if (calculateScore() >= bodies.size() && bodies.size() != 0 && !won){
                System.out.println("You Win");
                won = true;
                if(!LevelScreen.isMultiplayer()) {
                    showWinPopup(true);
                }else {
                    LevelScreen.finnished();
                }
            }
        }
        for (int i = 0; i < listOfCircleBodies.length; i++) {
            for (int j = 0; j < listOfCircleBodies[i].length; j++) {
                if (listOfCircleBodies[i][j] != null) {
                    //System.out.println(listOfCircleBodies[i][j].getNumber());
                    listOfCircleBodies[i][j].deactivateBodies();
                    listOfCircleBodies[i][j].changeGravity();
                    listOfCircleBodies[i][j].updateBalls();
                    if(listOfCircleBodies[i][j].isDyanmic() && listOfCircleBodies[i][j].getNumber() == 4){
                        listOfCircleBodies[i][j].setDyanmic(false);
                        setBodyType(listOfCircleBodies[i][j], listOfCircleBodies[i][j].getX(), listOfCircleBodies[i][j].getY(), listOfCircleBodies[i][j].getNumber(), listOfCircleBodies[i][j].getBodyType());
                    }  if(listOfCircleBodies[i][j].isDynamicSet() && listOfCircleBodies[i][j].getNumber() != 4){
                        listOfCircleBodies[i][j].setDyanmic(false);
                        listOfCircleBodies[i][j].setDynamicSet(false);
                        removegameBalls(bodies.get(bodies.size()-1));
                    }
                    if(listOfCircleBodies[i][j].isGravitySet && listOfCircleBodies[i][j].getNumber() != 4){
                        listOfCircleBodies[i][j].setGravity(false);
                        listOfCircleBodies[i][j].setGravitySet(false);
                        setGravity(bodies.get(bodies.size()-1),  listOfCircleBodies[i][j].isGravity());
                    }


                }
            }
        }
        if(destroyWorld){
            destroyWorld();
            destroyWorld = false;
        }

    }

    public void createWorld(float gravityX, float gravityY) {
        Vec2 gravity = new Vec2(gravityX, gravityY);
        world = new World(gravity);
    }

    public void destroyWorld() {
        // Destroy all bodies in the world
        world = new World(new Vec2(0f, 10)); // Gravity in m/s^2, adjusted for the direction
        bodies = new ArrayList<>();
        listOfCircleBodies = new Ball[WIDTH][HEIGHT];
        System.out.println("World Destroyed");
    }

    public Body[][] getListOfCircleBodies() {
        return listOfCircleBodies;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public World getWorld() {
        return world;
    }

    public Ball[][] getListOfBalls() {
        return listOfCircleBodies;
    }
    public void setEdit(Boolean edit) {
        isEdit = edit;
    }
    public Boolean getEdit() {
        return isEdit;
    }
    public void setEditNum(int num) {
        editNum = num;
    }
    public void setBodyType(Ball body, float x, float y, int number, BodyType newType) {
        // First, remove the current Ball from the world
          createBalls(x, y);


    }
        public void removegameBalls(Body body) {
            try {
                // First, remove the body from your collection (gameBalls)
                if (body != null){
                    bodies.remove(body);


                world.destroyBody(body); // Finally, destroy the body
            }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void setGravity(Body body, boolean gravity) {
            // First, remove the current Ball from the world
            if (gravity) {
                body.setGravityScale(-10.0f);
            } else {
                body.setGravityScale(10f);
            }
        }
        int calculateScore(){
        int score = 0;
        for(int i = 0; i< listOfCircleBodies.length; i++){
            for(int j = 0; j < listOfCircleBodies[i].length; j++){
               score += listOfCircleBodies[i][j].getCounter();

            }

        }


        return score;
        }
public void loadballs(){
    for(int i = 0; i< listOfCircleBodies.length; i++){
        for(int j = 0; j < listOfCircleBodies[i].length; j++){
            if(listOfCircleBodies[i][j].getNumber() == 4) {

                listOfCircleBodies[i][j].addbodiesToDeactivate(listOfCircleBodies[i][j].getBody());
                listOfCircleBodies[i][j].setDyanmic(false);
                listOfCircleBodies[i][j].setDynamicSet(false);
            }


        }

    }

}
   public void showWinPopup(boolean win) {

        if (context != null) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    if(!win) {
                        alertDialogBuilder.setTitle("You Lose");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    int score = calculateScore();
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("Level completed in " + duration + " milliseconds");
                    int timebonus = (int) (10000/duration);
                    score += timebonus;

                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("score", score);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(requestData);

                    if(!win){
                        score = 0;
                    }

                    String Url = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/player/"+MainActivity.getID()+"/money/"+score;

// Create a Volley DELETE request
                    StringRequest requesttt = new StringRequest(Request.Method.PUT, Url,
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
                                    System.out.println("Error: " + error);
                                }
                            }
                    );
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(requesttt);



                    String serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/map/updatePlayerMapHighScore/"+score+"/"+LevelScreen.getLevelNum()+"/"+MainActivity.getID();

// Create a Volley DELETE request

                        int finalScore = score;
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, serverUrl, requestData,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {

                                            String message = response.getString("message");
                                            System.out.println(message);
                                            if (LevelScreen.isMultiplayer() && win) {
                                                alertDialogBuilder.setTitle("You Win!" + " HighScore " + message + " " + finalScore + "   + " + finalScore + " Gold");


                                            } else if (!win) {
                                                alertDialogBuilder.setTitle("You Lose ");
                                            } else {
                                                alertDialogBuilder.setTitle("HighScore " + message + " " + finalScore + "   + " + finalScore + " Gold");

                                            }

                                            // Create and show the dialog
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();

                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }


                                    }

                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle the error response here
                                        System.out.println("Error: " + error);
                                    }
                                }
                        );
                         requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                        if(!win){
                        alertDialogBuilder.setTitle("You Lose ");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }







                     serverUrl = "http://nickm04@coms-309-021.class.las.iastate.edu:8080/api/mapContent/"+MainActivity.getID()+"/delete";

// Create a Volley DELETE request
                    StringRequest requestt = new StringRequest(Request.Method.DELETE, serverUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Handle the success response here
                                    // System.out.println("Response: " + response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle the error response here
                                    System.out.println("Error: " + error);
                                }
                            }
                    );
                     requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(requestt);



                    alertDialogBuilder.setPositiveButton("Replay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, LevelScreen.class);
                            intent.putExtra("selectedLevel", LevelScreen.getLevelNum());
                            context.startActivity(intent);

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Level Select", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                               ((Activity) context).finish();



                            Intent intent = new Intent(context, LevelList.class);
                            context.startActivity(intent);
                        }
                    });


                }
            });
        }

    }



    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
    public boolean getPlaying(){
        return playing;
    }
    public void setDestroyWorld(boolean destroyWorld) {
        this.destroyWorld = destroyWorld;
    }
    public boolean getWon(){
        return won;
    }
    public void setWon(boolean won) {
        this.won = won;
    }


    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public void resetscore() {
        for(int i = 0; i< listOfCircleBodies.length; i++){
            for(int j = 0; j < listOfCircleBodies[i].length; j++){
                listOfCircleBodies[i][j].setCounter(0);
            }

        }
        bodies.clear();
    }

}
