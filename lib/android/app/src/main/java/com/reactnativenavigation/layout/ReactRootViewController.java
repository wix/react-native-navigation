package com.reactnativenavigation.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.reactnativenavigation.react.NavigationEvent;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class ReactRootViewController extends ViewController {

	private final String name;
	private final NavigationOptions navigationOptions;
	private final ReactInstanceManager reactInstanceManager;
	private boolean attachedToReactInstance = false;
	private ReactRootView reactRootView;

	public ReactRootViewController(final Activity activity,
	                               final String id,
	                               final String name,
	                               final NavigationOptions navigationOptions,
	                               final ReactInstanceManager reactInstanceManager) {
		super(activity, id);
		this.name = name;
		this.navigationOptions = navigationOptions;
		this.reactInstanceManager = reactInstanceManager;
	}

	@Override
	public void destroy() {
		super.destroy();
		if (reactRootView != null) reactRootView.unmountReactApplication();
		reactRootView = null;
	}

	@Override
	public void onViewAppeared() {
		super.onViewAppeared();
		if (getParentStackController() != null)
			getParentStackController().setTitle(navigationOptions.title);
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).containerStart(getId());
	}

	@Override
	public void onViewDisappear() {
		super.onViewDisappear();
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).containerStop(getId());
	}

	@Override
	protected boolean isViewShown() {
		return super.isViewShown() && attachedToReactInstance;
	}

	@NonNull
	@Override
	protected View createView() {
		reactRootView = new ReactRootView(getActivity());
		Bundle opts = new Bundle();
		opts.putString("containerId", getId());
		reactRootView.startReactApplication(this.reactInstanceManager, this.name, opts);
		reactRootView.setEventListener(new ReactRootView.ReactRootViewEventListener() {
			@Override
			public void onAttachedToReactInstance(final ReactRootView reactRootView) {
				reactRootView.setEventListener(null);
				attachedToReactInstance = true;
			}
		});
		return reactRootView;
	}
}
