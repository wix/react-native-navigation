package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.*;

import org.junit.*;

public class ModalsTest extends BaseTest {

    @Test
    public void showModal() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Screen"));
    }

    @Test
    public void dismissModal() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Screen"));

        elementByText("DISMISS MODAL").click();
        assertMainShown();

    }

    @Test
    public void showMultipleModals() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));

        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 2"));

        elementByText("DISMISS MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));

        elementByText("DISMISS MODAL").click();
        assertMainShown();
    }

    @Test
    public void dismissUnknownContainerId() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));

        elementByText("DISMISS UNKNOWN MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));

        elementByText("DISMISS MODAL").click();
        assertMainShown();
    }

    @Test
    public void dismissModalByContainerIdWhenNotOnTop() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));

        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 2"));

        elementByText("DISMISS PREVIOUS MODAL").click();
        assertExists(By.text("Modal Stack Position: 2"));

        elementByText("DISMISS MODAL").click();
        assertMainShown();
    }

    @Test
    public void dismissAllPreviousModalsByIdWhenTheyAreBelowTopPresented() throws Exception {
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

    @Test
    public void dismissSomeModalByIdDeepInTheStack() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 2"));
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 3"));

        elementByText("DISMISS FIRST IN STACK").click();
        assertExists(By.text("Modal Stack Position: 3"));

        elementByText("DISMISS MODAL").click();
        assertExists(By.text("Modal Stack Position: 2"));

        elementByText("DISMISS MODAL").click();
        assertMainShown();
    }

    @Test
    public void dismissAllModals() throws Exception {
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 1"));
        elementByText("SHOW MODAL").click();
        assertExists(By.text("Modal Stack Position: 2"));

        elementByText("DISMISS ALL MODALS").click();
        assertMainShown();
    }

    @Test
    public void pushIntoModal() throws Exception {
        elementByText("SHOW MODAL").click();
        elementByText("PUSH SCREEN").click();
        assertExists(By.text("Pushed Screen"));
    }

    @Test
    public void backPopsModalStack() throws Exception {
        elementByText("SHOW MODAL").click();
        elementByText("PUSH SCREEN").click();
        elementByText("PUSH").click();
        device().pressBack();
        assertExists(By.text("Pushed Screen"));
    }
}
