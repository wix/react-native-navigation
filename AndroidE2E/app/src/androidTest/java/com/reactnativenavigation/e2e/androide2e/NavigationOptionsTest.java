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

//	@Test
//	public void testTopBarHidden() throws Exception {
//		elementByText("PUSH OPTIONS SCREEN").click();
//		int topWithNavigation = elementByText("HIDE TOP BAR").getVisibleBounds().top;
//		elementByText("HIDE TOP BAR").click();
//		int topWithoutNavigation = elementByText("HIDE TOP BAR").getVisibleBounds().top;
//		assertThat(topWithoutNavigation).isLessThan(topWithNavigation);
//		elementByText("SHOW TOP BAR").click();
//		assertExists(By.text("Static Title"));
//	}
}
