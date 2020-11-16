package com.reactnativenavigation.react;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;
import com.reactnativenavigation.NavigationApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Default implementation of {@link ReactNativeHost} that includes {@link NavigationPackage}
 * and user-defined additional packages.
 */
public abstract class NavigationReactNativeHost extends ReactNativeHost implements BundleDownloadListenerProvider {

    private @Nullable NavigationDevBundleDownloadListener bundleListener;
    private final DevBundleDownloadListener bundleListenerMediator = new DevBundleDownloadListenerAdapter() {
        @Override
        public void onSuccess() {
            if (bundleListener != null) {
                bundleListener.onSuccess();
            }
        }
    };

    @SuppressWarnings("WeakerAccess")
    public NavigationReactNativeHost(NavigationApplication application) {
        super(application);
    }

    @Override
    public void setBundleLoaderListener(NavigationDevBundleDownloadListener listener) {
        bundleListener = listener;
    }

    protected ReactInstanceManager createReactInstanceManager() {
        ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setJSMainModulePath(getJSMainModuleName())
                .setUseDeveloperSupport(getUseDeveloperSupport())
                .setRedBoxHandler(getRedBoxHandler())
                .setJavaScriptExecutorFactory(getJavaScriptExecutorFactory())
                .setUIImplementationProvider(getUIImplementationProvider())
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
                .setJSIModulesPackage(getJSIModulePackage())
                .setDevBundleDownloadListener(getDevBundleDownloadListener());

        for (ReactPackage reactPackage : getPackages()) {
            builder.addPackage(reactPackage);
        }

        String jsBundleFile = getJSBundleFile();
        if (jsBundleFile != null) {
            builder.setJSBundleFile(jsBundleFile);
        } else {
            builder.setBundleAssetName(Assertions.assertNotNull(getBundleAssetName()));
        }
        return builder.build();
    }

    @SuppressWarnings("WeakerAccess")
    @NonNull
    protected DevBundleDownloadListener getDevBundleDownloadListener() {
        return bundleListenerMediator;
    }
}
