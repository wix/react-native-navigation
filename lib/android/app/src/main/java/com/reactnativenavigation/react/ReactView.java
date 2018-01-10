package com.reactnativenavigation.react;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

@SuppressLint("ViewConstructor")
public class ReactView extends ReactRootView implements ContainerViewController.IReactView {

	private final ReactInstanceManager reactInstanceManager;
	private final String containerId;
	private final String containerName;
	private boolean isAttachedToReactInstance = false;

	public ReactView(final Context context, ReactInstanceManager reactInstanceManager, String containerId, String containerName) {
		super(context);
		this.reactInstanceManager = reactInstanceManager;
		this.containerId = containerId;
		this.containerName = containerName;
		start();
	}

	private void start() {
		setEventListener(reactRootView -> {
            reactRootView.setEventListener(null);
            isAttachedToReactInstance = true;
        });
		final Bundle opts = new Bundle();
		opts.putString("containerId", containerId);
		startReactApplication(reactInstanceManager, containerName, opts);
	}

	@Override
	public boolean isReady() {
		return isAttachedToReactInstance;
	}

	@Override
	public View asView() {
		return this;
	}

	@Override
	public void destroy() {
		unmountReactApplication();
	}

	@Override
	public void sendContainerStart() {
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).containerDidAppear(containerId);
	}

	@Override
	public void sendContainerStop() {
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).containerDidDisappear(containerId);
	}

    @Override
	public void sendOnNavigationButtonPressed(String buttonId) {
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).sendOnNavigationButtonPressed(containerId, buttonId);
	}
}
