package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.views.ContainerViewCreator;

public class SimpleContainerViewController extends ContainerViewController {
	public SimpleContainerViewController(final Activity activity, final String id) {
		super(activity, id, "theContainerName", new ContainerViewCreator(new TestContainerViewCreator()), new NavigationOptions());
	}
}
