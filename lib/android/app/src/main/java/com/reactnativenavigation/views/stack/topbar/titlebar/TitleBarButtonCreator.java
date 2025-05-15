package com.reactnativenavigation.views.stack.topbar.titlebar;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.options.ComponentOptions;

public class TitleBarButtonCreator {
	public TitleBarReactButtonView create(Activity activity, ComponentOptions component) {
        return new TitleBarReactButtonView(activity, component);
    }
}
