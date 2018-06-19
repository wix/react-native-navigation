package com.reactnativenavigation;

import android.app.Application;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.reactnativenavigation.react.NavigationReactNativeHost;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NavigationApplication extends Application implements ReactApplication {

	private ReactGateway reactGateway;
	public static NavigationApplication instance;
	final Map<String, ExternalComponentCreator> externalComponents = new HashMap<>();

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
	    return new ReactGateway(this, isDebug(), createReactNativeHost());
    }

    protected ReactNativeHost createReactNativeHost() {
        return new NavigationReactNativeHost(this);
    }

	public ReactGateway getReactGateway() {
		return reactGateway;
	}

	@Override
	public ReactNativeHost getReactNativeHost() {
		return getReactGateway().getReactNativeHost();
	}

    /**
     * Generally no need to override this; override for custom permission handling.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

	public abstract boolean isDebug();

    /**
     * Create a list of additional {@link ReactPackage}s to include. This method will only be called by
     * the default implementation of {@link #createReactGateway()}
     */
	@Nullable
	public abstract List<ReactPackage> createAdditionalReactPackages();

    /**
     * Register a native View which can be displayed using the given {@code name}
     * @param name Unique name used to register the native view
     * @param creator Used to create the view at runtime
     */
    @SuppressWarnings("unused")
    public void registerExternalComponent(String name, ExternalComponentCreator creator) {
        if (externalComponents.containsKey(name)) {
            throw new RuntimeException("A component has already been registered with this name: " + name);
        }
        externalComponents.put(name, creator);
    }

    public final Map<String, ExternalComponentCreator> getExternalComponents() {
        return externalComponents;
    }
}
