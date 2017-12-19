package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class SimpleContainerViewController extends ContainerViewController {
	public SimpleContainerViewController(final Activity activity, final String id) {
		super(activity, id, "theContainerName", new TestContainerViewCreator(), new NavigationOptions());
	}
}
