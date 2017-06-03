package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;
import android.view.KeyEvent;

import org.junit.Test;

public class ReactEventsTest extends BaseTest {

	@Test
	public void pressingMenuOpensDevMenu() throws Exception {
		launchTheApp();
		assertMainShown();
		device().pressKeyCode(KeyEvent.KEYCODE_MENU);
		assertExists(By.text("Debug JS Remotely"));
	}

	@Test
	public void pressingRTwiceInSuccessionReloadsReactNative() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));

		device().pressKeyCode(KeyEvent.KEYCODE_R);
		device().pressKeyCode(KeyEvent.KEYCODE_R);
		device().waitForIdle();
		assertMainShown();
	}

	@Test
	public void pressingRTwiceWithDelayDoesNothing() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));

		device().pressKeyCode(KeyEvent.KEYCODE_R);
		Thread.sleep(1500);
		device().pressKeyCode(KeyEvent.KEYCODE_R);
		assertExists(By.text("Pushed Screen"));
	}
}
