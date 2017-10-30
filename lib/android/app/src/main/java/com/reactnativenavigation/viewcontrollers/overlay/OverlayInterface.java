package com.reactnativenavigation.viewcontrollers.overlay;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;

public interface OverlayInterface {
	void init(Context context, OverlayOptions options);
	void show();
}
