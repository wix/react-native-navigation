package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.View;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.views.Container;
import com.reactnativenavigation.views.TopBar;

public class TestContainerLayout extends View implements ContainerViewController.IReactView, Container {

	private TopBar topBar;

	public TestContainerLayout(final Context context) {
		super(context);
		topBar = new TopBar(context);
	}

    @Override
	public boolean isReady() {
		return false;
	}

	@Override
	public View asView() {
		return this;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void sendContainerStart() {
	}

	@Override
	public void sendContainerStop() {
	}

    @Override
    public TopBar getTopBar() {
        return topBar;
    }
}
