package com.reactnativenavigation.playground;

import android.content.Context;

import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.react.NavigationPackage;
import com.reactnativenavigation.react.NavigationReactNativeHost;
import com.swmansion.reanimated.ReanimatedJSIModulePackage;
import com.facebook.react.bridge.JSIModulePackage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends NavigationApplication {

    private final ReactNativeHost mReactNativeHost =
            new NavigationReactNativeHost(this) {
                @Override
                protected JSIModulePackage getJSIModulePackage() {
                    return new ReanimatedJSIModulePackage();
                }
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
            };


    @Override
    public void onCreate() {
        super.onCreate();
        registerExternalComponent("RNNCustomComponent", new FragmentCreator());
        initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    private static void initializeFlipper(
            Context context, ReactInstanceManager reactInstanceManager) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> aClass = Class.forName("com.example.ReactNativeFlipper");
                aClass
                        .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
                        .invoke(null, context, reactInstanceManager);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
