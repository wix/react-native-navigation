package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

@SuppressLint("ViewConstructor")
public class TopTab extends FrameLayout implements IReactView {

	private final IReactView reactView;

	public TopTab(Context context, IReactView reactView) {
		super(context);
		this.reactView = reactView;
        addView(reactView.asView());
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
}
