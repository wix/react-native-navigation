package com.reactnativenavigation.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.ArrayList;
import java.util.List;

public class NavigationReactNativeHost extends ReactNativeHost {

	private final boolean isDebug;
	private final List<ReactPackage> additionalReactPackages;

	public NavigationReactNativeHost(Application application, boolean isDebug, final List<ReactPackage> additionalReactPackages) {
		super(application);
		this.isDebug = isDebug;
		this.additionalReactPackages = additionalReactPackages;
	}

	@Override
	public boolean getUseDeveloperSupport() {
		return isDebug;
	}

	@Override
	protected List<ReactPackage> getPackages() {
		List<ReactPackage> packages = new ArrayList<>();
		packages.add(new MainReactPackage());
		packages.add(new NavigationPackage(this));
		if (additionalReactPackages != null) {
			for (ReactPackage p : additionalReactPackages) {
				if (!(p instanceof MainReactPackage || p instanceof NavigationPackage)) {
					packages.add(p);
				}
			}
		}
		return packages;
	}
}
