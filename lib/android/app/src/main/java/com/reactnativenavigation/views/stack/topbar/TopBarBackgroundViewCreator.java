package com.reactnativenavigation.views.stack.topbar;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class TopBarBackgroundViewCreator implements ReactViewCreator {

	@Override
	public TopBarBackgroundView create(Activity activity, String componentId, String componentName) {
        return new TopBarBackgroundView(activity, componentId, componentName);
    }
}
