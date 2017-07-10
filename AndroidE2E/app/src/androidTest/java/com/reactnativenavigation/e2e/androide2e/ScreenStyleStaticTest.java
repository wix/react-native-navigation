package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class ScreenStyleStaticTest extends BaseTest {

	@Test
	public void declareNavigationStyleOnContainerComponent() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
		assertExists(By.text("Static Title"));
	}

	@Test
	public void setTitleDynamically() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
		assertExists(By.text("Static Title"));
		elementByText("DYNAMIC OPTIONS").click();
		assertExists(By.text("Dynamic Title"));
	}
}
