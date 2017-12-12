package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class ContainerView extends LinearLayout implements ContainerViewController.IReactView {

	private TopBar topBar;
	private ReactView reactView;

	public ContainerView(Context context, ReactView reactView) {
		super(context);
		this.topBar = new TopBar(context);
		this.reactView = reactView;
		initViews();
	}

	private void initViews() {
	    setOrientation(VERTICAL);
		addView(topBar);
		addView(reactView.asView());
	}

	public ContainerView(Context context) {
		super(context);
	}

	@Override
	public boolean isReady() {
		return reactView.isReady();
	}

	@Override
	public View asView() {
		return this;
	}

	@Override
	public void destroy() {
		reactView.destroy();
	}

	@Override
	public void sendContainerStart() {
		reactView.sendContainerStart();
	}

	@Override
	public void sendContainerStop() {
		reactView.sendContainerStop();
	}

	public TopBar getTopBar() {
		return topBar;
	}

	public ContainerViewController.IReactView getReactView() {
		return reactView;
	}
}
