package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.bridge.NavigationReactEventEmitter;
import com.reactnativenavigation.bridge.NavigationReactPackage;

import java.util.ArrayList;
import java.util.List;

public class NavigationReactGateway implements ReactGateway, ReactInstanceManager.ReactInstanceEventListener {

    private OnJsDevReloadListener onJsDevReloadListener;
    private ReactInstanceManager reactInstanceManager;
    private NavigationReactEventEmitter reactEventEmitter;

    public NavigationReactGateway() {
        reactInstanceManager = createReactInstanceManager();
    }

    @Override
    public void startReactContextOnceInBackgroundAndExecuteJS() {
        if (reactInstanceManager == null) {
            reactInstanceManager = createReactInstanceManager();
        }

        if (!reactInstanceManager.hasStartedCreatingInitialContext()) {
            reactInstanceManager.createReactContextInBackground();
        }
    }

    public boolean isInitialized() {
        return reactInstanceManager != null && reactInstanceManager.getCurrentReactContext() != null;
    }

    public ReactContext getReactContext() {
        return reactInstanceManager.getCurrentReactContext();
    }

    public NavigationReactEventEmitter getReactEventEmitter() {
        return reactEventEmitter;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return reactInstanceManager;
    }

    public void onBackPressed() {
        reactInstanceManager.onBackPressed();
    }

    public void onDestroyApp() {
        reactInstanceManager.onHostDestroy();
        reactInstanceManager.destroy();
        reactInstanceManager.removeReactInstanceEventListener(this);
        reactInstanceManager = null;
    }

    public void onPauseActivity() {
        reactInstanceManager.onHostPause();
        onJsDevReloadListener = null;
    }

    public void onResumeActivity(Activity activity, DefaultHardwareBackBtnHandler defaultHardwareBackBtnHandler, OnJsDevReloadListener onJsDevReloadListener) {
        this.onJsDevReloadListener = onJsDevReloadListener;
        reactInstanceManager.onHostResume(activity, defaultHardwareBackBtnHandler);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reactInstanceManager.onActivityResult(requestCode, resultCode, data);
    }

    private void replaceJsDevReloadListener(ReactInstanceManager manager) {
        new JsDevReloadListenerReplacer(manager, new JsDevReloadListenerReplacer.Listener() {
            @Override
            public void onJsDevReload() {
                if (onJsDevReloadListener != null)
                    onJsDevReloadListener.onJsDevReload();
            }
        }).replace();
    }

    private ReactInstanceManager createReactInstanceManager() {
        ReactInstanceManager.Builder builder = ReactInstanceManager.builder()
                .setUseOldBridge(true)
                .setApplication(NavigationApplication.instance)
                .setJSMainModuleName(NavigationApplication.instance.getJsEntryFileName())
                .setBundleAssetName(NavigationApplication.instance.getBundleAssetName())
                .setUseDeveloperSupport(NavigationApplication.instance.isDebug())
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME);

        for (ReactPackage reactPackage : createReactPackages()) {
            builder.addPackage(reactPackage);
        }

        ReactInstanceManager manager = builder.build();

        if (NavigationApplication.instance.isDebug()) {
            replaceJsDevReloadListener(manager);
        }

        manager.addReactInstanceEventListener(this);

        return manager;
    }

    private List<ReactPackage> createReactPackages() {
        List<ReactPackage> list = new ArrayList<>();
        list.add(new MainReactPackage());
        list.add(new NavigationReactPackage());
        addAdditionalReactPackagesIfNeeded(list);
        return list;
    }

    private void addAdditionalReactPackagesIfNeeded(List<ReactPackage> list) {
        List<ReactPackage> additionalReactPackages = NavigationApplication.instance.createAdditionalReactPackages();
        if (additionalReactPackages == null) {
            return;
        }

        for (ReactPackage reactPackage : additionalReactPackages) {
            if (reactPackage instanceof MainReactPackage)
                throw new RuntimeException("Do not create a new MainReactPackage. This is created for you.");
            if (reactPackage instanceof NavigationReactPackage)
                throw new RuntimeException("Do not create a new NavigationReactPackage. This is created for you.");
        }

        list.addAll(additionalReactPackages);
    }

    @Override
    public void onReactContextInitialized(ReactContext context) {
        reactEventEmitter = new NavigationReactEventEmitter(context);
        NavigationApplication.instance.onReactInitialized(context);
    }
}
