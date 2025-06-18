package com.reactnativenavigation.views.stack.topbar;

import android.content.Context;

import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class TopBarBackgroundViewCreator implements ReactViewCreator {

	@Override
	public TopBarBackgroundView create(Context context, String componentId, String componentName) {
        return new TopBarBackgroundView(context, componentId, componentName);
    }
}
