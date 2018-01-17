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
	public static NavigationApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
        instance = this;
        reactGateway = createReactGateway();
	}

    /**
     * Subclasses of NavigationApplication may override this method to create the singleton instance
     * of {@link ReactGateway}. For example, subclasses may wish to provide a custom {@link ReactNativeHost}
     * with the ReactGateway. This method will be called exactly once, in the application's {@link #onCreate()} method.
     *
     * Custom {@link ReactNativeHost}s must be sure to include {@link com.reactnativenavigation.react.NavigationPackage}
     *
     * @return a singleton {@link ReactGateway}
     */
	protected ReactGateway createReactGateway() {
	    return new ReactGateway(this, isDebug(), createAdditionalReactPackages());
    }

	public ReactGateway getReactGateway() {
		return reactGateway;
	}

	@Override
	public ReactNativeHost getReactNativeHost() {
		return getReactGateway().getReactNativeHost();
	}

	public abstract boolean isDebug();

    /**
     * Create a list of additional {@link ReactPackage}s to include. This method will only be called by
     * the default implementation of {@link #createReactGateway()}
     */
	@Nullable
	public abstract List<ReactPackage> createAdditionalReactPackages();
}
