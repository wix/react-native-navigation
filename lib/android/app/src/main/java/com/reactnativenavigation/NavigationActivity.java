package com.reactnativenavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.Navigator;

public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    protected Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = new Navigator(this, new ChildControllersRegistry(), new OverlayManager());
        getReactGateway().onActivityCreated(this);
        getReactGateway().addReloadListener(navigator);
        navigator.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(navigator.getView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReactGateway().onActivityResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getReactGateway().onActivityPaused(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigator.destroy();
        getReactGateway().removeReloadListener(navigator);
        getReactGateway().onActivityDestroyed(this);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        if (!navigator.handleBack(new CommandListenerAdapter())) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getReactGateway().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        getReactGateway().onBackPressed();
    }

    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        return getReactGateway().onKeyUp(keyCode) || super.onKeyUp(keyCode, event);
    }

    public ReactGateway getReactGateway() {
        return app().getReactGateway();
    }

    private NavigationApplication app() {
        return (NavigationApplication) getApplication();
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
