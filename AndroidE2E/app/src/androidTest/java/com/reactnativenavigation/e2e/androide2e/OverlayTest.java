package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class OverlayTest extends BaseTest {

	@Test
	public void testOverlayInterAlertAppear() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("SHOW OVERLAY INTER").click();
		assertExists(By.text("Test view"));
		elementByText("OK").click();
		assertExists(By.text("Overlay disappeared"));
        elementByText("OK").click();
	}

	@Test
	public void testOverlayNotInterAlertAppear() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("SHOW TOUCH THROUGH OVERLAY NOTINTER").click();
		assertExists(By.text("Test view"));
		elementByText("OK").click();
		assertExists(By.text("Overlay disappeared"));
        elementByText("OK").click();
	}
}
