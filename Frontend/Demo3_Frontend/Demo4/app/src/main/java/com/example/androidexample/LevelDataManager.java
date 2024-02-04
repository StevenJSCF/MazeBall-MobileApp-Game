package com.example.androidexample;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LevelDataManager {
    private static PhysicsSimulator physicsSimulator;
    private static final int WIDTH = 30;
private static final int HEIGHT = 30;
    public LevelDataManager(PhysicsSimulator physicsSimulator) {
        LevelDataManager.physicsSimulator = physicsSimulator;
    }

    // Serialize the world data (physics settings, bodies, etc.) into a JSON object
    public JSONObject serializeWorldData() {
        JSONObject worldData = new JSONObject();
        World world = physicsSimulator.getWorld();

        try {
            // Serialize key properties of the World class
            worldData.put("gravity_x", world.getGravity().x);
            worldData.put("gravity_y", world.getGravity().y);
            worldData.put("allow_sleep", world.isAllowSleep());
            worldData.put("warm_starting", world.isWarmStarting());
            worldData.put("continuous_physics", world.isContinuousPhysics());
            worldData.put("sub_stepping", world.isSubStepping());
            worldData.put("body_count", world.getBodyCount());
            worldData.put("joint_count", world.getJointCount());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return worldData;
    }

    public static String ArrToString(Integer[][] arr){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j <arr[i].length ; j++) {
                str.append(arr[i][j]);
            }
        }
        return str.toString();
    }

    public static Integer[][] serializeBodies(int height, int width){



        Ball[][] bodies = physicsSimulator.getListOfBalls();
        Integer[][] balls = new Integer[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width ; j++) {
                if (bodies[i][j] != null){
                    balls[i][j] = bodies[i][j].getNumber();
                } else{
                    balls[i][j] = 0;
                }
            }
        }

        return balls;
    }
    public JSONObject serializeBody(Body body) {
        JSONObject bodyData = new JSONObject();


        try {
            bodyData.put("type", body.getType());
            if(body.getType() == BodyType.DYNAMIC){
                bodyData.put("linearVelocity_x", body.getLinearVelocity().x);
                bodyData.put("linearVelocity_y", body.getLinearVelocity().y);
                bodyData.put("angularVelocity", body.getAngularVelocity());
                bodyData.put("linearDamping", body.getLinearDamping());
                bodyData.put("angularDamping", body.getAngularDamping());
                bodyData.put("gravityScale", body.getGravityScale());
            }


            // Store the x and y position
            bodyData.put("position_x", body.getPosition().x);
            bodyData.put("position_y", body.getPosition().y);

            // Store the angle
            bodyData.put("angle", body.getAngle());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bodyData;
    }


    static Integer[][] deserializeListOfCircleBodies(String listofCircles) throws JSONException {
        int numRows = WIDTH; // Replace WIDTH with the actual number of rows
        int numCols = HEIGHT; // Replace HEIGHT with the actual number of columns

        Integer[][] UsedPos = new Integer[numRows][numCols];
        Ball[][] listOfCircleBodies = new Ball[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                char charData = listofCircles.charAt(i * numCols + j);
                if(charData == '0'){
                    UsedPos[i][j] = 0;
                } else{
                   // System.out.println(charData);
                    UsedPos[i][j] = Character.getNumericValue(charData);
                   // System.out.println(UsedPos[i][j]);
                }
            }
        }

        //physicsSimulator.loadBallGrid(UsedPos);
        return UsedPos;
    }
    public World deserializeWorldData() {
        physicsSimulator.createWorld(0, 10);
        return physicsSimulator.getWorld();

    }



}
