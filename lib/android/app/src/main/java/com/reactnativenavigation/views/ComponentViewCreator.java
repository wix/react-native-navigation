package com.reactnativenavigation.views;

import android.app.*;

import com.facebook.react.*;
import com.facebook.react.uimanager.*;
import com.reactnativenavigation.react.*;
import com.reactnativenavigation.viewcontrollers.*;

public class ComponentViewCreator implements ComponentViewController.ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public ComponentViewCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
	}

	@Override
	public ComponentViewController.IReactView create(Activity activity, String componentId, String componentName) {
        ComponentViewController.IReactView reactView = new ReactComponentViewCreator(instanceManager).create(activity, componentId, componentName);
        return new ComponentLayout(activity, reactView, instanceManager.getCurrentReactContext().getNativeModule(UIManagerModule.class).getEventDispatcher());
	}
}
