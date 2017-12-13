package com.reactnativenavigation.views;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class ContainerViewCreator implements ContainerViewController.ReactViewCreator {

	private ContainerViewController.ReactViewCreator creator;

	public ContainerViewCreator(ContainerViewController.ReactViewCreator creator) {
		this.creator = creator;
	}

	@Override
	public ContainerViewController.IReactView create(Activity activity, String containerId, String containerName) {
        return new ContainerView(activity, creator.create(activity, containerId, containerName));
	}
}
