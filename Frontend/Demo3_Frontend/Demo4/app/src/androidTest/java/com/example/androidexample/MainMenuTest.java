package com.example.androidexample;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

public class MainMenuTest {

    @Rule
    public ActivityScenarioRule<MainMenu> activityRule = new ActivityScenarioRule<>(MainMenu.class);

    @Test
    public void testButtonClick_StartLevel() {
        // Click on the "Start Level" button
        Espresso.onView(ViewMatchers.withId(R.id.btnStartLevel)).perform(ViewActions.click());

        // Check if the LevelList activity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.levelListLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testButtonClick_EditLevel() {
        // Click on the "Edit Level" button
        Espresso.onView(ViewMatchers.withId(R.id.btnEditLevel)).perform(ViewActions.click());

        // Check if the LevelEditor activity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.levelEditorLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testButtonClick_LoadLevel() {
        // Click on the "Load Level" button
        Espresso.onView(ViewMatchers.withId(R.id.btnLoadLevel)).perform(ViewActions.click());

        // Check if the LevelScreen activity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.levelScreenLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testButtonClick_UserActivity() {
        // Click on the "User Activity" button
        Espresso.onView(ViewMatchers.withId(R.id.btnUserActivity)).perform(ViewActions.click());

        // Check if the MainScreenActivity activity is displayed
        //Espresso.onView(ViewMatchers.withId(R.id.activity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testButtonClick_Leaderboard() {
        // Click on the "Leaderboard" button
        Espresso.onView(ViewMatchers.withId(R.id.btnLeaderboard)).perform(ViewActions.click());

        // Check if the LeaderBoard activity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.leaderboardLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    // Add more tests as needed based on your application's features and logic
}

