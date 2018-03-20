package com.reactnativenavigation.views.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactView;

@SuppressLint("ViewConstructor")
public class TitleBarReactView extends ReactView {

    public TitleBarReactView(Context context, ReactInstanceManager reactInstanceManager, String componentId, String componentName) {
        super(context, reactInstanceManager, componentId, componentName);
    }
}
