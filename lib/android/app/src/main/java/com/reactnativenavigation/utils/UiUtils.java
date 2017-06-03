package com.reactnativenavigation.utils;

import android.view.View;
import android.view.ViewTreeObserver;

public class UiUtils {
	public static void runOnPreDrawOnce(final View view, final Runnable task) {
		view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				view.getViewTreeObserver().removeOnPreDrawListener(this);
				task.run();
				return true;
			}
		});
	}
}
