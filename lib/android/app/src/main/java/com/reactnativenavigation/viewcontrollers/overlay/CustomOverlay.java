package com.reactnativenavigation.viewcontrollers.overlay;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.CustomDialog;

public class CustomOverlay implements OverlayInterface {

	private CustomDialog dialog;

	@Override
	public CustomOverlay create(ViewController root, OverlayOptions options) {
		//TODO; implement

		ViewController viewController = options.getCustomView();
		dialog = new CustomDialog(root.getActivity(), viewController.getView());

		return this;
	}

	@Override
	public void show() {
		dialog.show();
	}

	@Override
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
