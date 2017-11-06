package com.reactnativenavigation.presentation;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.overlay.AlertOverlay;
import com.reactnativenavigation.viewcontrollers.overlay.CustomOverlay;
import com.reactnativenavigation.viewcontrollers.overlay.FabOverlay;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayFabric;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayInterface;
import com.reactnativenavigation.viewcontrollers.overlay.SnackbarOverlay;

public class OverlayPresenter {

	private OverlayInterface overlay;

	public OverlayPresenter(Context context, String type, OverlayOptions options) {
		this.overlay = OverlayFabric.create(type, context, options);
	}


	public void show() {
		overlay.show();
	}
}
