package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

public class ContainerLayout extends LinearLayout implements Container {

	private TopBar topBar;
	private IReactView reactView;

	public ContainerLayout(Context context, IReactView reactView) {
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

	public ContainerLayout(Context context) {
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

    @Override
	public TopBar getTopBar() {
		return topBar;
	}

	public IReactView getReactView() {
		return reactView;
	}
}
