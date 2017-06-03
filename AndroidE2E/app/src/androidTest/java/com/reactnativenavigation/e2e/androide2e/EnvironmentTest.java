package com.reactnativenavigation.e2e.androide2e;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class EnvironmentTest extends BaseTest {
	@Test
	public void instrumentationAndAssertJ() throws Exception {
		assertThat(getInstrumentation().getTargetContext().getPackageName()).isEqualTo("com.reactnativenavigation.e2e.androide2e");
	}
}
