package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.view.View;

import com.reactnativenavigation.layout.NavigationOptions;
import com.reactnativenavigation.layout.NavigationOptionsHolder;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class SimpleViewController extends ViewController implements NavigationOptionsHolder {

	public NavigationOptions lastNavigationOptions;

	public SimpleViewController(final Activity activity, String id) {
		super(activity, id);
	}

	@Override
	protected View createView() {
		return new View(getActivity());
	}

	@Override
	public String toString() {
		return "SimpleViewController " + getId();
	}

	@Override
	public void mergeNavigationOptions(NavigationOptions options) {
		lastNavigationOptions = options;
	}
}
