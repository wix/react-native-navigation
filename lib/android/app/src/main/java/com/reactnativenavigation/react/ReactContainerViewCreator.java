package com.reactnativenavigation.react;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.ReactViewCreator;

public class ReactContainerViewCreator implements ReactViewCreator {
	private ReactInstanceManager reactInstanceManager;

	public ReactContainerViewCreator(final ReactInstanceManager reactInstanceManager) {
		this.reactInstanceManager = reactInstanceManager;
	}

	@Override
	public ContainerViewController.IReactView create(final Activity activity, final String containerId, final String containerName) {
		return new ReactView(activity, reactInstanceManager, containerId, containerName);
	}
}
