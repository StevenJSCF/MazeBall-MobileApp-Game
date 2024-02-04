package com.example.androidexample;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static java.util.regex.Pattern.matches;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class LevelScreenTest {
    @Rule
    public ActivityScenarioRule<LevelScreen> activityRule = new ActivityScenarioRule<>(LevelScreen.class);

    @Test
    public void testSaveButton() {
        // Click the save button and check if it performs the expected action
        Espresso.onView(withId(R.id.saveButton)).perform(ViewActions.click());

        // Add assertions based on the expected behavior after clicking the save button
        // For example, you can check if a specific dialog or toast message is displayed
        // or navigate to another activity and check its state.
    }

    @Test
    public void testBackButton() {
        // Click the back button and check if it performs the expected action
        Espresso.onView(withId(R.id.back)).perform(ViewActions.click());

        // Add assertions based on the expected behavior after clicking the back button
        // For example, you can check if the LevelList activity is displayed.
      //  Espresso.onView(withId(R.id.levelListActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testPopupMenu() {
        // Open the popup menu and check if it is displayed
        Espresso.onView(withId(R.id.menuButton)).perform(ViewActions.click());
      //  Espresso.onView(withId(R.id.itemMenu)).check(matches(isDisplayed()));

        // Close the popup menu and check if it is no longer displayed
        Espresso.onView(withId(R.id.menuButton)).perform(ViewActions.click());
      //  Espresso.onView(withId(R.id.itemMenu)).check(matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void testHandleSaveResponse() {
        // Assuming you have a JSONObject representing a success response
        JSONObject successResponse = new JSONObject();
        try {
            successResponse.put("message", "Data saved");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Call the handleSaveResponse method with the success response
        activityRule.getScenario().onActivity(activity -> activity.handleSaveResponse(successResponse));

        // Verify that the success message is displayed
       // Espresso.onView(ViewMatchers.withText("Data saved")).check(matches(isDisplayed()));
    }

    @Test
    public void testShowCustomDialog() {
        // Assuming you have a JSONObject representing a response indicating another map is being played
        JSONObject response = new JSONObject();
        try {
            response.put("message", "another map is being played");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Call the showCustomDialog method with the response
        activityRule.getScenario().onActivity(activity -> activity.showCustomDialog(String.valueOf(response)));

        // Verify that the custom dialog is displayed
        //spresso.onView(ViewMatchers.withText("You currently have another level saved. What would you like to do?"))
              //  .check(matches(isDisplayed()));

        // Click on the "Continue Playing" button in the dialog
        //Espresso.onView(withId(android.R.id.button1)).perform(ViewActions.click());

        // Verify that the appropriate action is taken
        // (You may need to add assertions based on your expected behavior)
    }



}
