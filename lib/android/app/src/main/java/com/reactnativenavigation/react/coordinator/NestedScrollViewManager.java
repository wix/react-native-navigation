package com.reactnativenavigation.react.coordinator;


import android.view.View;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import java.util.List;

public class NestedScrollViewManager extends ViewGroupManager<RnnNestedScrollView> {

	private static final String CLASS_NAME = "RnnNestedScrollView";

	@Override
	public String getName() {
		return CLASS_NAME;
	}

	@Override
	protected RnnNestedScrollView createViewInstance(ThemedReactContext reactContext) {
		return new RnnNestedScrollView(reactContext);
	}

	@Override
	public void addView(RnnNestedScrollView parent, View child, int index) {
		super.addView(parent, child, index);
	}
}
