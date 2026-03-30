package com.reactnativenavigation.utils;

import android.os.Build;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

public class CompatUtils {
	private static final AtomicInteger viewId = new AtomicInteger(1);

	public static int generateViewId() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return View.generateViewId();
		} else {
			while (true) {
				final int result = viewId.get();
				// aapt-generated IDs have the high byte nonzero; clamp to the range under that.
				int newValue = result + 1;
				if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
				if (viewId.compareAndSet(result, newValue)) {
					return result;
				}
			}
		}
	}
}
