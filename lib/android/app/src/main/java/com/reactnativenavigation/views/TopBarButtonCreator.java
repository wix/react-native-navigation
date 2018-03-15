package com.reactnativenavigation.views;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.TopBarReactButtonView;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;

public class TopBarButtonCreator implements ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public TopBarButtonCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public IReactView create(Activity activity, String componentId, String componentName) {
        return new TopBarReactButtonView(activity, instanceManager, componentId, componentName);
    }
}
