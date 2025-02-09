package com.reactnativenavigation.playground;

import com.facebook.react.PackageList;
import com.facebook.react.ReactHost;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactHost;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.RNNToggles;
import com.reactnativenavigation.react.NavigationPackage;
import com.reactnativenavigation.react.NavigationReactNativeHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainApplication extends NavigationApplication {

    private final ReactNativeHost mReactNativeHost =
            new NavigationReactNativeHost(this) {
                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }

                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                public List<ReactPackage> getPackages() {
                    ArrayList<ReactPackage> packages = new PackageList(this).getPackages();
                    packages.add(new NavigationPackage(mReactNativeHost));
                    return packages;
                }

                @Override
                protected Boolean isHermesEnabled() {
                    return BuildConfig.IS_HERMES_ENABLED;
                }

                @Override
                protected boolean isNewArchEnabled() {
                    return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
                }
            };

    public MainApplication() {
        super(new HashMap<>() {{
            put(RNNToggles.TOP_BAR_COLOR_ANIMATION, true);
        }});
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerExternalComponent("RNNCustomComponent", new FragmentCreator());
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            DefaultNewArchitectureEntryPoint.load();
        }
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public ReactHost getReactHost() {
        return DefaultReactHost.getDefaultReactHost(this, getReactNativeHost());
    }
}
