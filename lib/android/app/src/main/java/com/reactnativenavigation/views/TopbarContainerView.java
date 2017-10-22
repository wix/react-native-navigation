package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class TopbarContainerView extends LinearLayout implements ContainerViewController.ContainerView {

	private TopBar topBar;
	private ContainerViewController.ContainerView containerView;

	public TopbarContainerView(Context context, ContainerViewController.ContainerView containerView) {
		super(context);
		this.topBar = new TopBar(context);
		this.containerView = containerView;

		initViews();
	}

	private void initViews() {
		setOrientation(LinearLayout.VERTICAL);
		addView(topBar);
		addView(containerView.asView());
	}

	public TopbarContainerView(Context context) {
		super(context);
	}

	@Override
	public boolean isReady() {
		return containerView.isReady();
	}

	@Override
	public View asView() {
		return this;
	}

	@Override
	public void destroy() {
		containerView.destroy();
	}

	@Override
	public void sendContainerStart() {
		containerView.sendContainerStart();
	}

	@Override
	public void sendContainerStop() {
		containerView.sendContainerStop();
	}

	public TopBar getTopBar() {
		return topBar;
	}

	public ContainerViewController.ContainerView getContainerView() {
		return containerView;
	}
}
