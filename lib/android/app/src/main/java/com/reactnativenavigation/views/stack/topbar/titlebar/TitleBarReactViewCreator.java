package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class TitleBarReactViewCreator implements ReactViewCreator {

    protected ReactInstanceManager instanceManager;

    public TitleBarReactViewCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public TitleBarReactView create(Activity activity, String componentId, String componentName) {
        return new TitleBarReactView(activity, instanceManager, componentId, componentName);
    }
}
