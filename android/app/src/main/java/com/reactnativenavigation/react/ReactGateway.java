package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.reactnativenavigation.bridge.NavigationReactEventEmitter;
import com.reactnativenavigation.controllers.NavigationActivity;

public interface ReactGateway {

    void startReactContextOnceInBackgroundAndExecuteJS();

    boolean isInitialized();

    ReactContext getReactContext();

    NavigationReactEventEmitter getReactEventEmitter();

    ReactInstanceManager getReactInstanceManager();

    void onResumeActivity(Activity activity, DefaultHardwareBackBtnHandler defaultHardwareBackBtnHandler);

    void onPauseActivity(NavigationActivity navigationActivity);

    void onDestroyApp(NavigationActivity navigationActivity);

    void onBackPressed();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);

    boolean onKeyUp(View currentFocus, int keyCode);
}
