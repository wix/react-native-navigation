package com.reactnativenavigation;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.facebook.react.common.ReactConstants;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EnvironmentTest extends BaseTest {
	@Test
	public void assertJ() {
		assertThat(1 + 2).isEqualTo(3).isGreaterThan(2).isLessThan(4).isNotNegative().isPositive().isNotZero();
	}

	@Test
	public void react() {
		assertThat(ReactConstants.TAG).isNotEmpty();
	}

	@Test
	public void supportV7AppCompat() {
		assertThat(AppCompatActivity.class).isNotNull();
	}

	@Test
	public void supportDesign() {
		assertThat(FloatingActionButton.class).isNotNull();
	}

	@Test
	public void androidR() {
		assertThat(R.string.bottom_sheet_behavior).isNotZero();
	}

	@Test
	public void ableToLoadApplication() throws Exception {
		assertThat(RuntimeEnvironment.application).isNotNull();
	}
}
