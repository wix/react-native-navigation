package com.reactnativenavigation.react;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;
import com.reactnativenavigation.NavigationApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class NavigationReactNativeHost extends DefaultReactNativeHost implements BundleDownloadListenerProvider {

    private @Nullable NavigationDevBundleDownloadListener bundleListener;
    private final DevBundleDownloadListener bundleListenerMediator = new DevBundleDownloadListenerAdapter() {
        @Override
        public void onSuccess() {
            if (bundleListener != null) {
                bundleListener.onSuccess();
            }
        }
    };

    public NavigationReactNativeHost(NavigationApplication application) {
        super(application);
    }

    @Override
    public void setBundleLoaderListener(NavigationDevBundleDownloadListener listener) {
        bundleListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    @NonNull
    protected DevBundleDownloadListener getDevBundleDownloadListener() {
        return bundleListenerMediator;
    }
}