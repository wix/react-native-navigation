package com.reactnativenavigation.views;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactComponentViewCreator;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;

public class TopTabCreator implements ComponentViewController.ReactViewCreator {


    private ReactInstanceManager instanceManager;

    public TopTabCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

	@Override
	public ComponentViewController.IReactView create(Activity activity, String componentId, String componentName) {
        ComponentViewController.IReactView reactView = new ReactComponentViewCreator(instanceManager).create(activity, componentId, componentName);
        return new TopTab(activity, reactView);
	}
}
