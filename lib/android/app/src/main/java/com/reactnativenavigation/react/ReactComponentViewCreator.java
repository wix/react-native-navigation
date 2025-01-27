package com.reactnativenavigation.react;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class ReactComponentViewCreator implements ReactViewCreator {
	public ReactComponentViewCreator() {
	}

	@Override
	public ReactView create(final Activity activity, final String componentId, final String componentName) {
		return new ReactView(activity, componentId, componentName);
	}
}
