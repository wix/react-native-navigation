package com.reactnativenavigation.utils;

import android.os.Handler;
import android.os.Looper;

public class UiThread {
	private static final Handler handler = new Handler(Looper.getMainLooper());

	public static void post(Runnable runnable) {
		handler.post(runnable);
	}

	public static void postDelayed(Runnable runnable, long millis) {
		handler.postDelayed(runnable, millis);
	}
}
