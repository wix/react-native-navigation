package com.reactnativenavigation.react;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;
import com.facebook.react.shell.MainReactPackage;
import com.reactnativenavigation.NavigationApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link ReactNativeHost} that includes {@link NavigationPackage}
 * and user-defined additional packages.
 */
public class NavigationReactNativeHost extends ReactNativeHost implements BundleDownloadListenerProvider {

    private final boolean isDebug;
    private final List<ReactPackage> additionalReactPackages;
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
        this(application, application.isDebug(), application.createAdditionalReactPackages());
    }

    @SuppressWarnings("WeakerAccess")
    public NavigationReactNativeHost(Application application, boolean isDebug, final List<ReactPackage> additionalReactPackages) {
        super(application);
        this.isDebug = isDebug;
        this.additionalReactPackages = additionalReactPackages;
    }

    @Override
    public void setBundleLoaderListener(NavigationDevBundleDownloadListener listener) {
        bundleListener = listener;
    }

    @Override
    public boolean getUseDeveloperSupport() {
        return isDebug;
    }

    @Override
    protected List<ReactPackage> getPackages() {
        List<ReactPackage> packages = new ArrayList<>();
        boolean hasMainReactPackage = false;
        packages.add(new NavigationPackage(this));
        if (additionalReactPackages != null) {
            for (ReactPackage p : additionalReactPackages) {
                if (!(p instanceof NavigationPackage)) {
                    packages.add(p);
                }
                if (p instanceof MainReactPackage) hasMainReactPackage = true;
            }
        }
        if (!hasMainReactPackage) {
            packages.add(new MainReactPackage());
        }
        return packages;
    }

    protected ReactInstanceManager createReactInstanceManager() {
        ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setJSMainModulePath(getJSMainModuleName())
                .setUseDeveloperSupport(getUseDeveloperSupport())
                .setRedBoxHandler(getRedBoxHandler())
                .setJavaScriptExecutorFactory(getJavaScriptExecutorFactory())
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
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
