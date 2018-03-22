package com.reactnativenavigation.views;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;

public class TopBarButtonCreator implements ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public TopBarButtonCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public TopBarReactButtonView create(Activity activity, String componentId, String componentName) {
        return new TopBarReactButtonView(activity, instanceManager, componentId, componentName);
    }
}
