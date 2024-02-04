package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SystemTest {

    @Rule
    public ActivityScenarioRule<UserSelectActivity> activityRule = new ActivityScenarioRule<>(UserSelectActivity.class);

    @Test
    public void testUser1Button() {
        init(); // Initialize Intents

        // Perform a click on user1Button
        onView(withId(R.id.user1_button)).perform(click());

        // Verify that an Intent to MainScreenActivity was sent with the correct extras
        intended(hasExtra("userUrl", "http://coms-309-021.class.las.iastate.edu:8080/player/6"));
        intended(hasExtra("userWebSocketUrl", "ws://coms-309-021.class.las.iastate.edu:8080/active/6"));

        release(); // Release Intents
    }

    @Test
    public void testUser2Button() {
        init(); // Initialize Intents

        // Perform a click on user2Button
        onView(withId(R.id.user2_button)).perform(click());

        // Verify that an Intent to MainScreenActivity was sent with the correct extras
        intended(hasExtra("userUrl", "http://coms-309-021.class.las.iastate.edu:8080/player/7"));
        intended(hasExtra("userWebSocketUrl", "ws://coms-309-021.class.las.iastate.edu:8080/active/7"));

        release(); // Release Intents
    }

    // Additional tests can be written for other UI components and interactions
}
