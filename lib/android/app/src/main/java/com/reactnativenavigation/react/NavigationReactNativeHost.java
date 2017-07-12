package com.reactnativenavigation.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

public class NavigationReactNativeHost extends ReactNativeHost {

	private final boolean isDebug;

	public NavigationReactNativeHost(Application application, boolean isDebug) {
		super(application);
		this.isDebug = isDebug;
	}

	@Override
	public boolean getUseDeveloperSupport() {
		return isDebug;
	}

	@Override
	protected List<ReactPackage> getPackages() {
		return Arrays.<ReactPackage>asList(
				new MainReactPackage(),
				new NavigationPackage(this)
		);
	}
}
