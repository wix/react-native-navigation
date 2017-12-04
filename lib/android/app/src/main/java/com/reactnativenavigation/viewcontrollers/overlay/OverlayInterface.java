package com.reactnativenavigation.viewcontrollers.overlay;


import android.app.Activity;
import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;

public interface OverlayInterface {
	OverlayInterface create(ViewController viewController, OverlayOptions options);
	void show();
	void dismiss();
}
