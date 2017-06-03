package com.reactnativenavigation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 25, constants = BuildConfig.class, manifest = "/../../../../../src/test/AndroidManifest.xml")
public abstract class BaseTest {
	@Before
	public void beforeEach() {
		//
	}

	@After
	public void afterEach() {
		//
	}

	public Activity newActivity() {
		return Robolectric.setupActivity(Activity.class);
	}

	public void assertIsChildById(ViewGroup parent, View child) {
		assertThat(parent).isNotNull();
		assertThat(child).isNotNull();
		assertThat(child.getId()).isNotZero().isPositive();
		assertThat(parent.findViewById(child.getId())).isNotNull().isEqualTo(child);
	}

	public void assertNotChildOf(ViewGroup parent, View child) {
		assertThat(parent).isNotNull();
		assertThat(child).isNotNull();
		assertThat(child.getId()).isNotZero().isPositive();
		assertThat(parent.findViewById(child.getId())).isNull();
	}
}
