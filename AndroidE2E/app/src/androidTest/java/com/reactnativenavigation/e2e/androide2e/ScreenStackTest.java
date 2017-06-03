package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class ScreenStackTest extends BaseTest {

	@Test
	public void pushAndPopScreen() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));
		elementByText("POP").click();
		assertMainShown();
	}

	@Test
	public void popScreenDeepInTheStack() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));
		assertExists(By.text("Stack Position: 1"));
		elementByText("PUSH").click();
		assertExists(By.text("Stack Position: 2"));
		elementByText("POP PREVIOUS").click();
		assertExists(By.text("Stack Position: 2"));
		elementByText("POP").click();
		assertMainShown();
	}

	@Test
	public void popToId() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		elementByText("PUSH").click();
		elementByText("PUSH").click();
		assertExists(By.text("Stack Position: 3"));
		elementByText("POP TO STACK POSITION 1").click();
		assertExists(By.text("Stack Position: 1"));
	}

	@Test
	public void popToRoot() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		elementByText("PUSH").click();
		elementByText("PUSH").click();
		elementByText("POP TO ROOT").click();
		assertMainShown();
	}
}
