package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

public class ContainerView extends LinearLayout implements IReactView {

	private TopBar topBar;
	private IReactView reactView;

	public ContainerView(Context context, IReactView reactView) {
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

	public IReactView getReactView() {
		return reactView;
	}
}
