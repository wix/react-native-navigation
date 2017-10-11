package com.reactnativenavigation.views;

import android.app.Activity;
import android.widget.LinearLayout;

import com.reactnativenavigation.react.ReactContainerViewCreator;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

/**
 * Created by romanko on 10/10/17.
 */

public class TopbarContainerViewCreator implements ContainerViewController.ContainerViewCreator {

	private ContainerViewController.ContainerViewCreator creator;

	public TopbarContainerViewCreator(ContainerViewController.ContainerViewCreator creator) {
		this.creator = creator;
	}

	@Override
	public ContainerViewController.ContainerView create(Activity activity, String containerId, String containerName) {
		TopBar topBar = new TopBar(activity);
		topBar.setId(CompatUtils.generateViewId());

		ContainerViewController.ContainerView containerView = creator.create(activity, containerId, containerName);

		TopbarContainerView root = new TopbarContainerView(activity, topBar, containerView);
		return root;

	}
}
