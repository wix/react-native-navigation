package com.reactnativenavigation.viewcontrollers.overlay;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.views.CustomDialog;

public class CustomOverlay implements OverlayInterface {

	private CustomDialog dialog;

	@Override
	public CustomOverlay create(Context context, OverlayOptions options) {
		//TODO; implement

		ContainerViewController.ContainerView customView = options.getCustomView();
		dialog = new CustomDialog(context, customView.asView());

		return this;
	}

	@Override
	public void show() {
		dialog.show();
	}
}
