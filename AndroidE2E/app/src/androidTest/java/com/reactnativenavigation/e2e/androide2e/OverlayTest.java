package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Test;

public class OverlayTest extends BaseTest {

	@Test
	public void testOverlayAlertAppear() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("SHOW OVERLAY").click();
		assertExists(By.text("Test view"));
        assetDismissed();
	}

    @Test
	public void testOverlayNotInterceptingTouchEvents() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("SHOW TOUCH THROUGH OVERLAY").click();
		assertExists(By.text("Test view"));
        elementByText("DYNAMIC OPTIONS").click();
        assertExists(By.text("Dynamic Title"));
        assetDismissed();
	}

    private void assetDismissed() throws UiObjectNotFoundException {
        elementByText("OK").click();
        assertExists(By.text("Overlay disappeared"));
        elementByText("OK").click();
    }
}
