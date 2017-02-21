package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.reactnativenavigation.bridge.NavigationReactEventEmitter;

public interface ReactGateway {

    void startReactContextOnceInBackgroundAndExecuteJS();

    boolean isInitialized();

    ReactContext getReactContext();

    NavigationReactEventEmitter getReactEventEmitter();

    ReactInstanceManager getReactInstanceManager();

    void onResumeActivity(Activity activity, DefaultHardwareBackBtnHandler defaultHardwareBackBtnHandler);

    void onPauseActivity();

    void onDestroyApp();

    void onBackPressed();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean hasStartedCreatingContext();

    void onNewIntent(Intent intent);
}
