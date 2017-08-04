package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class SwitchToTabTest extends BaseTest {

	@Test
	public void switchToTab() throws Exception {
		elementByText("SWITCH TO TAB BASED APP").click();
		assertExists(By.text("This is tab 1"));
		elementByText("SWITCH TAB").click();
		assertExists(By.text("This is tab 2"));
	}

    @Test
	public void switchToTabDeep() throws Exception {
		elementByText("SWITCH TO TAB BASED APP").click();
		assertExists(By.text("This is tab 1"));
		elementByText("PUSH").click();
		elementByText("SWITCH TAB").click();
		assertExists(By.text("This is tab 2"));
	}
}
