package com.reactnativenavigation.react;

import android.content.Context;

import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class ReactComponentViewCreator implements ReactViewCreator {
	@Override
	public ReactView create(final Context context, final String componentId, final String componentName) {
		return new ReactView(context, componentId, componentName);
	}
}
