package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.*;

import org.junit.*;

public class BackHandlerTest extends BaseTest {
    @Test
    public void overrideHardwareBackButton() throws Exception {
        elementByText("BACK HANDLER").click();
        assertExists(By.text("Back Handler Screen"));

        elementByText("ADD BACK HANDLER").click();
        device().pressBack();
        assertExists(By.text("Back Handler Screen"));


        elementByText("REMOVE BACK HANDLER").click();
        device().pressBack();
        assertMainShown();
    }
}
