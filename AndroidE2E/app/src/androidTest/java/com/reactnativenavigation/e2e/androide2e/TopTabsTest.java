package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class TopTabsTest extends BaseTest {

    @Test
    public void tabsCanBeSwiped() throws Exception {
        elementByText("PUSH TOP TABS SCREEN").click();
        assertExists(By.text("This is top tab 1"));
        device().swipe(400, 400, 10, 400, 2);
        assertExists(By.text("This is top tab 2"));
    }
}
