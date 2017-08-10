package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class ScreenLifecycleTest extends BaseTest {

	@Test
	public void didAppearDidDisappear() throws Exception {
		elementByText("PUSH LIFECYCLE SCREEN").click();
		assertExists(By.text("didAppear"));
		elementByText("PUSH TO TEST DIDDISAPPEAR").click();
		assertExists(By.text("didDisappear"));
	}

	@Test
	public void unmountIsCalledWhenPopped() throws Exception {
		elementByText("PUSH LIFECYCLE SCREEN").click();
		assertExists(By.text("didAppear"));

		device().pressBack();

		assertExists(By.text("componentWillUnmount"));
		elementByText("OK").click();
		assertExists(By.text("didDisappear"));
		elementByText("OK").click();
		assertMainShown();
	}
}
