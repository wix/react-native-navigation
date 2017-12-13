package com.reactnativenavigation.views;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class TopTabCreator implements ContainerViewController.ReactViewCreator {

	private ContainerViewController.ReactViewCreator creator;

	public TopTabCreator(ContainerViewController.ReactViewCreator creator) {
		this.creator = creator;
	}

	@Override
	public ContainerViewController.IReactView create(Activity activity, String containerId, String containerName) {
        return new TopTab(activity, creator.create(activity, containerId, containerName));
	}
}
