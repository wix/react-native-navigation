package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.content.Context;

import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class TitleBarReactViewCreator implements ReactViewCreator {

	@Override
	public TitleBarReactView create(Context context, String componentId, String componentName) {
        return new TitleBarReactView(context, componentId, componentName);
    }
}
