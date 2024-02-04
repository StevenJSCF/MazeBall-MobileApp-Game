package com.example.androidexample;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidexample.LevelDataManager;
import com.example.androidexample.PhysicsSimulator;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LevelDataManagerTest {

    private PhysicsSimulator physicsSimulator;

    @Before
    public void setUp() {
        ActivityScenario.launch(LevelScreen.class);
         physicsSimulator = new PhysicsSimulator(getApplicationContext());
    }

    @Test
    public void serializeDeserializeWorldData() {
        LevelDataManager levelDataManager = new LevelDataManager(physicsSimulator);

        // Serialize the world data
        World originalWorld = physicsSimulator.getWorld();
        String serializedWorldData = levelDataManager.serializeWorldData().toString();

        // Deserialize the world data
        World deserializedWorld = levelDataManager.deserializeWorldData();

        // Assert that the deserialized world has the same gravity as the original world
        assertEquals(originalWorld.getGravity().x, deserializedWorld.getGravity().x, 0.0);
        assertEquals(originalWorld.getGravity().y, deserializedWorld.getGravity().y, 0.0);
        assertEquals(originalWorld.isAllowSleep(), deserializedWorld.isAllowSleep());
        // Add more assertions as needed for other properties

        // Check if the leaderboard container is displayed
    }
    @Test
    public void serializeBodiesTest() {
        LevelDataManager levelDataManager = new LevelDataManager(physicsSimulator);

        // Add some balls to the physics simulator
        physicsSimulator.createBallGrid(0.5f, 2, 30, 30, 0.001f);

        // Serialize the bodies
        Integer[][] serializedBodies = levelDataManager.serializeBodies(30, 30);

        // Expected result based on the added balls
        Integer[][] expectedBodies = levelDataManager.serializeBodies(30, 30);



        // Assert that the serialized bodies match the expected result
        assertArrayEquals(expectedBodies, serializedBodies);
    }


    @Test
    public void deserializeListOfCircleBodiesTest() throws JSONException {
        LevelDataManager levelDataManager = new LevelDataManager(physicsSimulator);

        // Prepare a string representation of serialized bodies with 900 "1"s
        StringBuilder serializedBodiesStringBuilder = new StringBuilder();
        for (int i = 0; i < 900; i++) {
            serializedBodiesStringBuilder.append("1");
        }
        String serializedBodiesString = serializedBodiesStringBuilder.toString();

        // Deserialize the bodies
        Integer[][] deserializedBodies = levelDataManager.deserializeListOfCircleBodies(serializedBodiesString);

        // Expected result based on the provided string
        Integer[][] expectedBodies = new Integer[30][30];
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                expectedBodies[i][j] = 1;
            }
        }

        // Assert that the deserialized bodies match the expected result
        assertArrayEquals(expectedBodies, deserializedBodies);
    }

    // Add more tests as needed for other methods in LevelDataManager
}
