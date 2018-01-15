package com.reactnativenavigation.viewcontrollers.overlay;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;


public class DialogViewController extends ViewController {

	private ComponentViewController.ReactViewCreator viewCreator;
	private String componentName;

	public DialogViewController(Activity activity, String id, String componentName, ComponentViewController.ReactViewCreator viewCreator) {
		super(activity, id);
		this.viewCreator = viewCreator;
		this.componentName = componentName;
	}

	@NonNull
	@Override
	protected View createView() {
		return viewCreator.create(getActivity(), getId(), componentName).asView();
	}
}
