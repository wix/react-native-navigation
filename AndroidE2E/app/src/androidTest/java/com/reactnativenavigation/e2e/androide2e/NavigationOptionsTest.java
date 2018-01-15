package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

public class NavigationOptionsTest extends BaseTest {

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

    @Test
    public void testTopBarHidden() throws Exception {
        elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("HIDE TOP BAR").click();
        assertThat(elementByText("Static Title").exists()).isFalse();
        elementByText("SHOW TOP BAR").click();
        assertExists(By.text("Static Title"));
    }

    @Test
    public void testRightButtons() throws Exception {
        elementByText("PUSH OPTIONS SCREEN").click();
        assertExists(By.text("ONE"));
        elementByText("ONE").click();
    }

    @Test
    public void testTopBarCollapse() throws Exception {
        elementByText("PUSH OPTIONS SCREEN").click();
        elementByText("SCROLLVIEW SCREEN").click();
        assertExists(By.text("Collapse"));
        elementByText("TOGGLE TOP BAR HIDE ON SCROLL").click();
        swipeUp();
        assertThat(device().hasObject(By.text("Collapse"))).isFalse();
    }
}
