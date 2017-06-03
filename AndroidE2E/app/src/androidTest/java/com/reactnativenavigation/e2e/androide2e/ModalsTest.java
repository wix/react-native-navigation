package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Ignore;
import org.junit.Test;

public class ModalsTest extends BaseTest {
	@Ignore
	@Test
	public void showModal() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Screen"));
	}

	@Ignore
	@Test
	public void dismissModal() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Screen"));

		elementByText("DISMISS MODAL").click();
		assertMainShown();

	}

	@Ignore
	@Test
	public void showMultipleModals() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));

		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));

		elementByText("DISMISS MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));

		elementByText("DISMISS MODAL").click();
		assertMainShown();
	}

	@Ignore
	@Test
	public void dismissUnknownContainerId() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));

		elementByText("DISMISS UNKNOWN MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));

		elementByText("DISMISS MODAL").click();
		assertMainShown();
	}

	@Ignore
	@Test
	public void dismissModalByContainerIdWhenNotOnTop() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));

		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));

		elementByText("DISMISS PREVIOUS MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));

		elementByText("DISMISS MODAL").click();
		assertMainShown();
	}

	@Ignore
	@Test
	public void dismissAllPreviousModalsByIdWhenTheyAreBelowTopPresented() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 3"));

		elementByText("DISMISS ALL PREVIOUS MODALS").click();
		assertExists(By.text("Modal Stack Position: 3"));

		elementByText("DISMISS MODAL").click();
		assertMainShown();
	}

	@Ignore
	@Test
	public void dismissSomeModalByIdDeepInTheStack() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 3"));

		elementByText("DISMISS FIRST IN THE STACK").click();
		assertExists(By.text("Modal Stack Position: 3"));

		elementByText("DISMISS MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));

		elementByText("DISMISS MODAL").click();
		assertMainShown();
	}

	@Ignore
	@Test
	public void dismissAllModals() throws Exception {
		launchTheApp();
		assertMainShown();
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 1"));
		elementByText("SHOW MODAL").click();
		assertExists(By.text("Modal Stack Position: 2"));

		elementByText("DISMISS ALL MODALS").click();
		assertMainShown();
	}
}
