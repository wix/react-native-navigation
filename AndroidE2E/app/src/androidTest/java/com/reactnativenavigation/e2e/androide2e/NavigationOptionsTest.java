package com.reactnativenavigation.e2e.androide2e;

import android.support.test.uiautomator.By;

import org.junit.Test;
import static org.assertj.core.api.Java6Assertions.assertThat;

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
	public void toggleNavigationBar() throws Exception {
		elementByText("PUSH OPTIONS SCREEN").click();
		int topWithNavigation = elementByText("SETTOPBARHIDDEN").getVisibleBounds().top;
		elementByText("SETTOPBARHIDDEN").click();
		int topWithoutNavigation = elementByText("SETTOPBARHIDDEN").getVisibleBounds().top;
		assertThat(topWithoutNavigation).isLessThan(topWithNavigation);
		elementByText("DYNAMIC OPTIONS").click();
		assertExists(By.text("Dynamic Title"));
	}
}
