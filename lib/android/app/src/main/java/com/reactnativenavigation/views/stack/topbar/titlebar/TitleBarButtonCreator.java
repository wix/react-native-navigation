package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.options.ComponentOptions;

public class TitleBarButtonCreator {

    private ReactInstanceManager instanceManager;

    public TitleBarButtonCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	public TitleBarReactButtonView create(Activity activity, ComponentOptions component) {
        return new TitleBarReactButtonView(activity, instanceManager, component);
    }
}
