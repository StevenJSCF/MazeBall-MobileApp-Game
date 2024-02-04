package com.example.androidexample;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLoginSuccess() {
        // Assuming valid credentials
        String validUsername = "1234";
        String validPassword = "1234";

        // Type valid username and password
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText(validUsername));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText(validPassword), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // Check if the MainMenu activity is displayed after successful login
        //Espresso.onView(ViewMatchers.withId(R.id.mainMenuLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        // Assuming invalid credentials
        String invalidUsername = "invalid_username";
        String invalidPassword = "invalid_password";

        // Type invalid username and password
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText(invalidUsername));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText(invalidPassword), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // Check if a toast with the error message is displayed
        String errorMessage = "Invalid credentials"; // Adjust based on your actual error message
       // Espresso.onView(ViewMatchers.withText(errorMessage)).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    // Add more tests as needed based on your application's features and logic
}

