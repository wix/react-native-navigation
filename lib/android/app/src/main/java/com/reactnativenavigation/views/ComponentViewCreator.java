package com.reactnativenavigation.views;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactComponentViewCreator;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.controllers.viewcontroller.IReactView;
import com.reactnativenavigation.controllers.viewcontroller.ReactViewCreator;

public class ComponentViewCreator implements ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public ComponentViewCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public IReactView create(Activity activity, String componentId, String componentName) {
        ReactView reactView = new ReactComponentViewCreator(instanceManager).create(activity, componentId, componentName);
        return new ComponentLayout(activity, reactView);
	}
}
