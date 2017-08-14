package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.ContainerViewCreator;

public class TestContainerViewCreator implements ContainerViewCreator {
	@Override
	public ContainerViewController.ContainerView create(final Activity activity, final String containerId, final String containerName) {
		return new TestContainerView(activity);
	}
}
