package com.reactnativenavigation.views;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class TopbarContainerViewCreator implements ContainerViewController.ContainerViewCreator {

	private ContainerViewController.ContainerViewCreator creator;

	public TopbarContainerViewCreator(ContainerViewController.ContainerViewCreator creator) {
		this.creator = creator;
	}

	@Override
	public ContainerViewController.ContainerView create(Activity activity, String containerId, String containerName) {
		ContainerViewController.ContainerView containerView = creator.create(activity, containerId, containerName);

		TopbarContainerView root = new TopbarContainerView(activity, containerView);
		return root;

	}
}
