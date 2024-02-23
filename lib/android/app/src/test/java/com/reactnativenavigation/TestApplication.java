package com.reactnativenavigation;

import android.app.*;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;

import java.util.Collections;
import java.util.List;

public class TestApplication extends Application implements ReactApplication {
    private final ReactNativeHost host = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return true;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Collections.emptyList();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.Theme_AppCompat);
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return host;
    }
}
