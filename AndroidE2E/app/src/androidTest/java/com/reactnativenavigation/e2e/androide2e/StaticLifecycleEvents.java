package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class StaticLifecycleEvents extends BaseTest {
    @Test
    public void didAppearDidDisappear() throws Exception {
        elementByText("STATIC LIFECYCLE EVENTS").click();
        assertExists(By.text("Static Lifecycle Events"));
        assertExists(By.text("didAppear | navigation.playground.StaticLifecycleOverlay"));
        elementByText("PUSH").click();
        assertExists(By.text("didAppear | navigation.playground.PushedScreen"));
        assertExists(By.text("didDisappear | navigation.playground.WelcomeScreen"));
    }
}
