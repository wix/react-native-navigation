package com.reactnativenavigation.playground;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;

import com.facebook.react.shell.MainReactPackage;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.react.NavigationPackage;
import com.reactnativenavigation.react.NavigationReactNativeHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

public class MainApplication extends NavigationApplication {

    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
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
                    return Arrays.asList(new MainReactPackage(), new NavigationPackage(mReactNativeHost));
                }
            };


    @Override
    public void onCreate() {
        super.onCreate();
        registerExternalComponent("RNNCustomComponent", new FragmentCreator());
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }
}
