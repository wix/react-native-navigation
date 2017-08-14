package com.reactnativenavigation.react;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.ContainerViewCreator;

public class ReactContainerViewCreator implements ContainerViewCreator {
	private ReactInstanceManager reactInstanceManager;

	public ReactContainerViewCreator(final ReactInstanceManager reactInstanceManager) {
		this.reactInstanceManager = reactInstanceManager;
	}

	@Override
	public ContainerViewController.ContainerView create(final Activity activity, final String containerId, final String containerName) {
		return new ReactContainerView(activity, reactInstanceManager, containerId, containerName);
	}
}
