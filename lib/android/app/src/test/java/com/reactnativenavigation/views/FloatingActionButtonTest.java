package com.reactnativenavigation.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.react.coordinator.FloatingActionButtonView;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class FloatingActionButtonTest extends BaseTest {

	private FloatingActionButtonView uut;

	@Override
	public void beforeEach() {
		super.beforeEach();
		uut = new FloatingActionButtonView(newActivity());
	}

	@Test
	public void setGravity() throws Exception {
		assertThat(((CoordinatorLayout.LayoutParams) uut.getLayoutParams()).gravity & Gravity.RIGHT).isNotEqualTo(Gravity.RIGHT);

		uut.setGravityRight(true);
		assertThat(((CoordinatorLayout.LayoutParams) uut.getLayoutParams()).gravity & Gravity.RIGHT).isEqualTo(Gravity.RIGHT);
	}

	@Test
	public void changeGravity() throws Exception {
		uut.setGravityLeft(true);
		assertThat(((CoordinatorLayout.LayoutParams) uut.getLayoutParams()).gravity & Gravity.LEFT).isEqualTo(Gravity.LEFT);

		uut.setGravityRight(true);
		assertThat(((CoordinatorLayout.LayoutParams) uut.getLayoutParams()).gravity & Gravity.LEFT).isNotEqualTo(Gravity.LEFT);
		assertThat(((CoordinatorLayout.LayoutParams) uut.getLayoutParams()).gravity & Gravity.RIGHT).isEqualTo(Gravity.RIGHT);
	}

	@Test
	public void setElevation() throws Exception {
		assertThat(uut.getElevation()).isNotEqualTo(20f);

		uut.setFabElevation(20f);
		assertThat(uut.getElevation()).isEqualTo(20f);
	}
}
