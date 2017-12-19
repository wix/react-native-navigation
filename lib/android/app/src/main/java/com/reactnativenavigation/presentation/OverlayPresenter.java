package com.reactnativenavigation.presentation;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayFactory;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayInterface;

public class OverlayPresenter {

	private OverlayInterface overlay;

	public OverlayPresenter(ViewController viewController, String type, OverlayOptions options) {
		this.overlay = OverlayFactory.create(type, viewController, options);
	}


	public void show() {
		overlay.show();
	}

	public void dismiss() {
		overlay.dismiss();
	}
}
