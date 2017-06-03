package com.reactnativenavigation.utils;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CompatUtilsTest extends BaseTest {

	@Test
	public void generateViewId() throws Exception {
		assertThat(CompatUtils.generateViewId())
				.isPositive()
				.isNotEqualTo(CompatUtils.generateViewId());
	}
}
