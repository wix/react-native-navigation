package com.reactnativenavigation.viewcontrollers.overlay;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;

public interface OverlayInterface {
	OverlayInterface create(Context context, OverlayOptions options);
	void show();
}
