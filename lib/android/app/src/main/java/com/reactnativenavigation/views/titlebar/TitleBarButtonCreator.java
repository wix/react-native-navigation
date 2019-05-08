package com.reactnativenavigation.views.titlebar;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;

public class TitleBarButtonCreator implements ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public TitleBarButtonCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public TitleBarReactButtonView create(Activity activity, String componentId, String componentName) {
        return new TitleBarReactButtonView(activity, instanceManager, componentId, componentName);
    }
}
