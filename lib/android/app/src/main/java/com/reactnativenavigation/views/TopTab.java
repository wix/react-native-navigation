package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

public class TopTab extends FrameLayout implements IReactView {

	private IReactView reactView;

	public TopTab(Context context, IReactView reactView) {
		super(context);
		this.reactView = reactView;
		initViews();
	}

	private void initViews() {
		addView(reactView.asView());
	}

	public TopTab(Context context) {
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

	public IReactView getReactView() {
		return reactView;
	}
}
