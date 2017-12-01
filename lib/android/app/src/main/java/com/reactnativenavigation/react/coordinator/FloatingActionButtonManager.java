package com.reactnativenavigation.react.coordinator;


import android.graphics.Color;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class FloatingActionButtonManager extends SimpleViewManager<FloatingActionButtonView> {

	private final static String CLASS_NAME = "RNNFloatingActionButton";

	@Override
	public String getName() {
		return CLASS_NAME;
	}

	@Override
	protected FloatingActionButtonView createViewInstance(ThemedReactContext reactContext) {
		return new FloatingActionButtonView(reactContext);
	}

	@ReactProp(name = "gravityTop")
	public void setGravityTop(FloatingActionButtonView fab, boolean gravityTop) {
		fab.setGravityTop(gravityTop);
	}

	@ReactProp(name = "gravityBottom", defaultBoolean = true)
	public void setGravityBottom(FloatingActionButtonView fab, boolean gravityBottom) {
		fab.setGravityBottom(gravityBottom);
	}

	@ReactProp(name = "gravityRight", defaultBoolean = true)
	public void setGravityRight(final FloatingActionButtonView fab, boolean gravityRight) {
		fab.setGravityRight(gravityRight);
	}

	@ReactProp(name = "gravityLeft")
	public void setGravityLeft(final FloatingActionButtonView fab, boolean gravityLeft) {
		fab.setGravityLeft(gravityLeft);
	}

	@ReactProp(name = "icon")
	public void setIcon(FloatingActionButtonView view, String uri) {
		view.setIcon(uri);
	}

	@ReactProp(name = "backgroundColor")
	public void setBackgroundColor(FloatingActionButtonView view, String backgroundColor) {
		int color = Color.parseColor(backgroundColor);
		view.setBackground(color);
	}

	@ReactProp(name = "hidden")
	public void setHidden(FloatingActionButtonView view, boolean hidden) {
		view.setHidden(hidden);
	}

	@ReactProp(name = "elevation", defaultFloat = 18)
	public void setElevation(FloatingActionButtonView view, float elevation) {
		view.setFabElevation(elevation);
	}
}
