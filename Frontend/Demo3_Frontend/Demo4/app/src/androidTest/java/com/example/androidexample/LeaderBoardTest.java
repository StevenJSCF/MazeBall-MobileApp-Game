package com.example.androidexample;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidexample.LeaderBoard;
import com.example.androidexample.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LeaderBoardTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(LeaderBoard.class);
    }

    @Test
    public void leaderboardDataDisplayed() {
        // Wait for the data to load (you may need to customize this depending on your app's behavior)
        // For simplicity, you can use Thread.sleep, but in a real test, consider using IdlingResource or other synchronization mechanisms.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the leaderboard container is displayed
        onView(withId(R.id.leaderboardContainer)).check(matches(isDisplayed()));

        // Check if at least one entryTextView is displayed
        //onView(ViewMatchers.withId(R.id.entryTextView)).check(matches(isDisplayed()));
    }

    // Add more tests as needed for different scenarios
}
