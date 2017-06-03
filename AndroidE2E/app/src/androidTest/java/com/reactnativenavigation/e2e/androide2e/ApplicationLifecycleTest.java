package com.reactnativenavigation.e2e.androide2e;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.test.uiautomator.By;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationLifecycleTest extends BaseTest {

	@Test
	public void _1_acceptsOverlayPermissions_ShowsWelcomeScreen() throws Exception {
		launchTheApp();
		assertMainShown();
	}

	@Test
	public void _2_relaunchFromBackground() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));

		device().pressHome();
		device().pressRecentApps();
		elementByText("Playground").click();

		assertExists(By.text("Pushed Screen"));
	}

	@Test
	public void _3_relaunchAfterClose() throws Exception {
		launchTheApp();
		assertMainShown();

		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));

		device().pressBack();

		launchTheApp();
		assertMainShown();
	}

	@Test
	public void _4_deviceOrientationDoesNotDestroyActivity() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));

		device().setOrientationLeft();
		Thread.sleep(100);

		assertExists(By.text("Pushed Screen"));
	}

	@Test
	public void _5_relaunchAfterActivityKilledBySystem() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("PUSH").click();
		assertExists(By.text("Pushed Screen"));

		killAppSaveInstanceState_ByTogglingPermissions();

		device().pressRecentApps();
		elementByText("Playground").click();
		assertMainShown();
	}

	private void killAppSaveInstanceState_ByTogglingPermissions() throws Exception {
		device().pressHome();

		device().waitForIdle();
		launchAppInfoSettings();
		device().waitForIdle();

		elementByText("Permissions").click();
		elementByText("Storage").click();
		elementByText("Storage").click();
		device().pressBack();
		device().pressBack();
		device().pressHome();
		device().waitForIdle();
	}

	private void launchAppInfoSettings() {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		Uri uri = Uri.fromParts("package", PACKAGE_NAME, null);
		intent.setData(uri);
		getInstrumentation().getTargetContext().startActivity(intent);
	}
}
