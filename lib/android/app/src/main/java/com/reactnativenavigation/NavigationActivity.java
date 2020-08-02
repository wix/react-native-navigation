package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.reactnativenavigation.views.pip.PIPStates;


public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, PermissionAwareActivity, JsDevReloadHandler.ReloadListener {
    @Nullable
    private PermissionListener mPermissionListener;

    protected Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (navigator == null || navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
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

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            navigator.setContentLayout(findViewById(android.R.id.content));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            getReactGateway().onActivityResumed(this);
            if (PIPActivity.Companion.getINSTANCE() != null) {
                PIPActivity.Companion.getINSTANCE().finish();
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            if (!getReactGateway().onNewIntent(intent)) {
                super.onNewIntent(intent);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            getReactGateway().onActivityPaused(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            navigator.destroy();
            getReactGateway().onActivityDestroyed(this);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (navigator.getPipMode() != PIPStates.NOT_STARTED) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                navigator.onPictureInPictureModeChanged(true, null);
                enterPictureInPictureMode(new PictureInPictureParams.Builder().build());
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        navigator.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
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
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            navigator.destroyViews();
        }
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
