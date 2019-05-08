package com.reactnativenavigation.views;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactComponentViewCreator;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;

public class ComponentViewCreator implements ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public ComponentViewCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public IReactView create(Activity activity, String componentId, String componentName) {
        IReactView reactView = new ReactComponentViewCreator(instanceManager).create(activity, componentId, componentName);
        return new ComponentLayout(activity, reactView);
	}
}
