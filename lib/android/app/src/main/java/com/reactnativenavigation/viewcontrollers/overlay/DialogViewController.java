package com.reactnativenavigation.viewcontrollers.overlay;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;


public class DialogViewController extends ViewController {

	private ContainerViewController.ContainerViewCreator viewCreator;
	private String containerName;

	public DialogViewController(Activity activity, String id, String containerName, ContainerViewController.ContainerViewCreator viewCreator) {
		super(activity, id);
		this.viewCreator = viewCreator;
		this.containerName = containerName;
	}

	@NonNull
	@Override
	protected View createView() {
		return viewCreator.create(getActivity(), getId(), containerName).asView();
	}
}
