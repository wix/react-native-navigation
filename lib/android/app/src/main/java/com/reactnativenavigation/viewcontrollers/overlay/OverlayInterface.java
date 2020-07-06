package com.reactnativenavigation.viewcontrollers.overlay;


import com.reactnativenavigation.options.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

public interface OverlayInterface {
	OverlayInterface create(ViewController viewController, OverlayOptions options);
	void show();
	void dismiss();
}
