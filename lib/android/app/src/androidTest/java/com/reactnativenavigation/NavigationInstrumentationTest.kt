package com.reactnativenavigation

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example instrumentation test for React Native Navigation
 * These tests run on an actual device or emulator
 */
@RunWith(AndroidJUnit4::class)
class NavigationInstrumentationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TestActivity::class.java)

    @Test
    fun whenAppLaunches_thenRootComponentIsDisplayed() {
        // Wait for React Native to load
        Thread.sleep(2000)

        // Verify that the root component is displayed
        // Note: You'll need to replace R.id.root with your actual root view ID
        onView(withId(android.R.id.content))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenNavigatingToScreen_thenScreenIsDisplayed() {
        // Wait for React Native to load
        Thread.sleep(2000)

        // Example of how to test navigation
        // This is a placeholder - you'll need to implement actual navigation
        // and verify the expected screen is displayed
        // For example:
        // onView(withId(R.id.navigate_button)).perform(click())
        // onView(withId(R.id.screen_title)).check(matches(withText("Expected Screen")))
    }
} 