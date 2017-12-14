package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.ReactViewCreator;

public class TestContainerViewCreator implements ReactViewCreator {
	@Override
	public ContainerViewController.IReactView create(final Activity activity, final String containerId, final String containerName) {
		return new TestContainerLayout(activity);
	}
}
