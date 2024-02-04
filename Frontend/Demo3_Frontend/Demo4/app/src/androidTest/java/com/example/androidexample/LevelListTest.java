package com.example.androidexample;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class LevelListTest {

    @Rule
    public ActivityScenarioRule<LevelList> activityRule = new ActivityScenarioRule<>(LevelList.class);

    @Test
    public void testPageUpButton() {
        // Click the page up button
        Espresso.onView(ViewMatchers.withId(R.id.pageUpButton)).perform(ViewActions.click());

        // Check if the current page has decreased
        // You may need to adjust the expected values based on your implementation
        Espresso.onView(ViewMatchers.withText("Level 1")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testPageDownButton() {
        // Click the page down button
        Espresso.onView(ViewMatchers.withId(R.id.pageDownButton)).perform(ViewActions.click());

        // Check if the current page has increased
        // You may need to adjust the expected values based on your implementation
        Espresso.onView(ViewMatchers.withText("Level 11")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testLevelButtonClick() {
        // Click a level button
        Espresso.onView(ViewMatchers.withText("Level 1")).perform(ViewActions.click());

        // Check if the intent to LevelScreen is launched
        // You may need to adjust the expected values based on your implementation
        Espresso.onView(ViewMatchers.withId(R.id.levelScreenLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
