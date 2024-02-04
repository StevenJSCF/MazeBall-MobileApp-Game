package com.example.androidexample;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class StoreActivityTest {


    @Rule
    public ActivityScenarioRule<StoreActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), StoreActivity.class)
                    .putExtra("userUrl", "http://example.com/user/123")
    );

    @Test
    public void testCoinDeductionAndItemPurchase() {
        // Assuming the initial coins are 100 as per your getCoins() method
        Espresso.onView(withId(R.id.coins)).check(matches(withText("100")));

        // Click on a button to buy an item
        Espresso.onView(withId(R.id.buyReversButton)).perform(click());

        // Check if coins are deducted correctly (assuming each item costs 10 coins)
        Espresso.onView(withId(R.id.coins)).check(matches(withText("90")));


//        // Assuming the initial coins are 100 as per your getCoins() method
//         Espresso.onView(withId(R.id.coins)).check(matches(withText("90")));
//
//        // Click on a button to buy an item
//    Espresso.onView(withId(R.id.buyTensButton)).perform(click());
//
//        // Check if coins are deducted correctly (assuming each item costs 10 coins)
//    Espresso.onView(withId(R.id.coins)).check(matches(withText("80")));
//
//
//
//        // Assuming the initial coins are 100 as per your getCoins() method
//        Espresso.onView(withId(R.id.coins)).check(matches(withText("80")));
//
//        // Click on a button to buy an item
//      Espresso.onView(withId(R.id.buyTwentyButton)).perform(click());
//
//        // Check if coins are deducted correctly (assuming each item costs 10 coins)
//     Espresso.onView(withId(R.id.coins)).check(matches(withText("70")));


    }

    // Additional tests for other buttons and functionalities
}
