package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class OverlayTest extends BaseTest {

	@Test
	public void testOverlayAlertAppear() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
		elementByText("SHOW CUSTOM ALERT").click();
		assertExists(By.text("Test view"));
		elementByText("OK").click();
		assertExists(By.text("Static Title"));
	}

	@Test
	public void testSnackbarAppear() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
		elementByText("SHOW SNACKBAR").click();
		assertExists(By.text("Test!"));
	}
}
