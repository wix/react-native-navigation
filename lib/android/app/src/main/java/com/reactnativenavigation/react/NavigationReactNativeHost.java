package com.reactnativenavigation.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link ReactNativeHost} that includes {@link NavigationPackage}
 * and user-defined additional packages.
 */
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
		// Please note that users may provide their own implementations of ReactNativeHost. Any additional
		// package requirements should be documented in the changelog.
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
