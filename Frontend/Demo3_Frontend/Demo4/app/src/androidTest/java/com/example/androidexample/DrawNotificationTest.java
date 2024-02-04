package com.example.androidexample;

import android.content.Context;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.androidexample.Drawnotification;
import com.example.androidexample.LevelEditor;
import com.example.androidexample.LevelScreen;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DrawNotificationTest {

    @Rule
    public ActivityScenarioRule<LevelScreen> levelScreenActivityScenarioRule =
            new ActivityScenarioRule<>(LevelScreen.class);



    @Test
    public void testShowGameInvitationDialog() {
        // Assuming the invitation message is "userId,userName"
        String invitationMessage = "123,John";

        // Launch the LevelScreen activity
        ActivityScenario<LevelScreen> activityScenario = levelScreenActivityScenarioRule.getScenario();

        // Show the game invitation dialog
        activityScenario.onActivity(activity -> {
            Drawnotification.showGameInvitationDialog(activity, invitationMessage);
        });

        // Verify that the dialog is displayed
       // onView(withText("John has invited you to edit a game!")).check(matches(isDisplayed()));

// Click on the Accept button
        //onView(withId(R.drawable.ic_custom_check)).perform(click());


        // No need to manually release resources, Espresso handles it automatically
    }

}

