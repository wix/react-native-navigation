package com.reactnativenavigation;

import android.app.Application;
import android.support.annotation.Nullable;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.reactnativenavigation.react.ReactGateway;

import java.util.List;

public abstract class NavigationApplication extends Application implements ReactApplication {

	private ReactGateway reactGateway;

	@Override
	public void onCreate() {
		super.onCreate();
		reactGateway = new ReactGateway(this, isDebug(), createAdditionalReactPackages());
	}

	public ReactGateway getReactGateway() {
		return reactGateway;
	}

	@Override
	public ReactNativeHost getReactNativeHost() {
		return getReactGateway().getReactNativeHost();
	}

	public abstract boolean isDebug();

	@Nullable
	public abstract List<ReactPackage> createAdditionalReactPackages();
}
