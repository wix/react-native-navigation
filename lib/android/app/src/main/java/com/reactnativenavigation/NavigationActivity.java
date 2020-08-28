package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    private int anotherActivityCount = 0;
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
        getApplication().registerActivityLifecycleCallbacks(lifecycleCallback);

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED && navigator.getPipMode() != PIPStates.NATIVE_MOUNT_START) {
            navigator.setContentLayout(findViewById(android.R.id.content));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED && navigator.getPipMode() != PIPStates.NATIVE_MOUNT_START) {
            getReactGateway().onActivityResumed(this);
        }
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
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED && navigator.getPipMode() != PIPStates.NATIVE_MOUNT_START) {
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
        getApplication().unregisterActivityLifecycleCallbacks(lifecycleCallback);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (navigator.getPipMode() != PIPStates.NOT_STARTED && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (canEnterPiPMode()) {
                navigator.updatePIPState(PIPStates.NATIVE_MOUNT_START);
                enterPictureInPictureMode(navigator.getPictureInPictureParams());
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            navigator.updatePIPState(PIPStates.NATIVE_MOUNTED);
        } else {
            navigator.updatePIPState(PIPStates.CUSTOM_MOUNTED);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        if (!navigator.handleBack(new CommandListenerAdapter())) {
            super.onBackPressed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (navigator.getPipMode() == PIPStates.NATIVE_MOUNTED && NavigationActivity.this.anotherActivityCount <= 0) {
            finish();
        } else if (navigator.getPipMode() == PIPStates.NATIVE_MOUNT_START) {
            navigator.resetPIP();
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

    private Application.ActivityLifecycleCallbacks lifecycleCallback = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity != NavigationActivity.this) {
                NavigationActivity.this.anotherActivityCount++;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (activity != NavigationActivity.this) {
                NavigationActivity.this.anotherActivityCount--;
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean canEnterPiPMode() {
        // AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        return true;// (AppOpsManager.MODE_ALLOWED == appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, Process.myUid(), getPackageName()));
    }
}
