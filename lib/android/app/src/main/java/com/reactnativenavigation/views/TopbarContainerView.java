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
		this.containerView = containerView;
		this.topBar = new TopBar(context, this.containerView);

		initViews();
	}

	private void initViews() {
		setOrientation(LinearLayout.VERTICAL);
		addView(topBar);
		addView(this.containerView.asView());
	}

	public TopbarContainerView(Context context) {
		super(context);
	}

	@Override
	public boolean isReady() {
		return this.containerView.isReady();
	}

	@Override
	public View asView() {
		return this;
	}

	@Override
	public void destroy() {
		this.containerView.destroy();
	}

	@Override
	public void sendContainerStart() {
		this.containerView.sendContainerStart();
	}

	@Override
	public void sendContainerStop() {
		this.containerView.sendContainerStop();
	}

	@Override
	public void sendOnNavigationButtonPressed(String id, String buttonId) {
		this.containerView.sendOnNavigationButtonPressed(id, buttonId);
	}

	@Override
	public String getContainerId() {
		return this.containerView.getContainerId();
	}

	public TopBar getTopBar() {
		return topBar;
	}

	public ContainerViewController.ContainerView getContainerView() {
		return this.containerView;
	}
}
