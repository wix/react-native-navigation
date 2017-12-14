package com.reactnativenavigation.views;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactContainerViewCreator;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class TopTabCreator implements ContainerViewController.ReactViewCreator {


    private ReactInstanceManager instanceManager;

    public TopTabCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

	@Override
	public ContainerViewController.IReactView create(Activity activity, String containerId, String containerName) {
        ContainerViewController.IReactView reactView = new ReactContainerViewCreator(instanceManager).create(activity, containerId, containerName);
        return new TopTab(activity, reactView);
	}
}
