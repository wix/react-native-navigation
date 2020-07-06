package com.reactnativenavigation.views.stack.topbar;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class TopBarBackgroundViewCreator implements ReactViewCreator {

    protected ReactInstanceManager instanceManager;

    public TopBarBackgroundViewCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public TopBarBackgroundView create(Activity activity, String componentId, String componentName) {
        return new TopBarBackgroundView(activity, instanceManager, componentId, componentName);
    }
}
