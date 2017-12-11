package com.reactnativenavigation.react.coordinator;


import android.view.View;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import java.util.List;

public class RnnNestedScrollViewManager extends ViewGroupManager<RnnNestedScrollView> {

	private static final String CLASS_NAME = "RnnNestedScrollView";

	@Override
	public String getName() {
		return CLASS_NAME;
	}

	@Override
	protected RnnNestedScrollView createViewInstance(ThemedReactContext reactContext) {
		return new RnnNestedScrollView(reactContext);
	}
}
