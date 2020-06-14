package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.presentation.RootPresenter;
import com.reactnativenavigation.react.JsDevReloadHandler;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, PermissionAwareActivity, JsDevReloadHandler.ReloadListener {
    @Nullable
    private PermissionListener mPermissionListener;
    
    protected Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) {
            return;
        }
        addDefaultSplashLayout();
        navigator = new Navigator(this,
                new ChildControllersRegistry(),
                new ModalStack(this),
                new OverlayManager(),
                new RootPresenter(this)
        );
        navigator.bindViews();
        getReactGateway().onActivityCreated(this);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigator.setContentLayout(findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReactGateway().onActivityResumed(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (!getReactGateway().onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getReactGateway().onActivityPaused(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigator != null) {
            navigator.destroy();
        }
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

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        NavigationApplication.instance.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            mPermissionListener = null;
        }
    }

    @Override
    public void onReload() {
        navigator.destroyViews();
    }

    protected void addDefaultSplashLayout() {
        View view = new View(this);
        view.setBackgroundColor(Color.WHITE);
        setContentView(view);
    }

    public void onCatalystInstanceDestroy() {
        runOnUiThread(() -> navigator.destroyViews());
    }
}
