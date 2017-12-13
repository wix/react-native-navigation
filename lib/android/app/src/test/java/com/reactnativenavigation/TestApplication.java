package com.reactnativenavigation;

import android.app.Application;

public class TestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		setTheme(R.style.Theme_AppCompat);
	}
}
