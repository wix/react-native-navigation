package com.reactnativenavigation.controllers.overlay;


import com.reactnativenavigation.options.OverlayOptions;
import com.reactnativenavigation.controllers.viewcontroller.ViewController;

public interface OverlayInterface {
	OverlayInterface create(ViewController viewController, OverlayOptions options);
	void show();
	void dismiss();
}
