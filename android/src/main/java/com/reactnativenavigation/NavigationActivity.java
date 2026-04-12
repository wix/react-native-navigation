package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayManager;
import com.reactnativenavigation.viewcontrollers.statusbar.StatusBarPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter;
import com.reactnativenavigation.react.JsDevReloadHandler;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, PermissionAwareActivity, JsDevReloadHandler.ReloadListener {
    @Nullable
    private PermissionListener mPermissionListener;

    protected Navigator navigator;

    private OnBackPressedCallback callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        enableEdgeToEdge();
        super.onCreate(savedInstanceState);
        if (isFinishing()) {
            return;
        }
        addDefaultSplashLayout();
        navigator = new Navigator(this,
                new ChildControllersRegistry(),
                new ModalStack(this),
                new OverlayManager(),
                new RootPresenter()
        );
        navigator.bindViews();
        getReactGateway().onActivityCreated(this);
        setBackPressedCallback();
        StatusBarPresenter.Companion.init(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getReactGateway().onConfigurationChanged(this, newConfig);
        navigator.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ViewGroup contentLayout = findViewById(android.R.id.content);
        navigator.setContentLayout(contentLayout);
        SystemUiUtils.setupSystemBarBackgrounds(this, contentLayout);
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
        SystemUiUtils.tearDown();
        if (navigator != null) {
            navigator.destroy();
        }
        getReactGateway().onActivityDestroyed(this);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        if (!navigator.handleBack(new CommandListenerAdapter())) {
            callback.setEnabled(false);
            NavigationActivity.super.onBackPressed();
            callback.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getReactGateway().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        return getReactGateway().onKeyUp(this, keyCode) || super.onKeyUp(keyCode, event);
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NavigationApplication.instance.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            mPermissionListener = null;
        }
    }

    @Override
    public void onReload() {
        navigator.destroyViews();
    }

    /**
     * Controls when edge-to-edge is enabled. Override to customize the decision logic.
     * Call {@link #activateEdgeToEdge()} from your override to enable edge-to-edge.
     * <p>
     * The default implementation enables edge-to-edge only if the app theme sets
     * {@code windowOptOutEdgeToEdgeEnforcement} to {@code false} (API 35+).
     * Called at the start of onCreate, before super.onCreate.
     */
    protected void enableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            android.content.res.TypedArray a = getTheme().obtainStyledAttributes(
                    new int[]{android.R.attr.windowOptOutEdgeToEdgeEnforcement});
            boolean optOut = a.getBoolean(0, true);
            a.recycle();
            if (!optOut) {
                activateEdgeToEdge();
            }
        }
    }

    /**
     * Enables edge-to-edge display and notifies the navigation framework.
     * Call this from {@link #enableEdgeToEdge()} overrides instead of
     * calling {@code EdgeToEdge.enable()} directly.
     */
    protected void activateEdgeToEdge() {
        EdgeToEdge.enable(this);
        SystemUiUtils.activateEdgeToEdge();
    }

    protected void addDefaultSplashLayout() {
        View view = new View(this);
        setContentView(view);
    }

    public void onCatalystInstanceDestroy() {
        runOnUiThread(() -> navigator.destroyViews());
    }

    private void setBackPressedCallback() {
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getReactGateway().onBackPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
