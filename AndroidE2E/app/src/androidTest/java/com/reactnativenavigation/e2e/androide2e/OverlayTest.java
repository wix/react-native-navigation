package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

public class OverlayTest extends BaseTest {

	@Test
	public void testOverlayAlertAppear() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("SHOW OVERLAY").click();
		assertExists(By.text("Test view"));
		elementByText("OK").click();
		assertExists(By.text("Overlay disappeared"));
        elementByText("OK").click();
	}
}
