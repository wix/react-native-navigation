package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.react.JsDevReloadHandler;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.utils.ILogger;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter;
import com.reactnativenavigation.views.pip.PIPStates;


public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, PermissionAwareActivity, JsDevReloadHandler.ReloadListener {
    @Nullable
    private PermissionListener mPermissionListener;
    private int anotherActivityCount = 0;
    protected Navigator navigator;
    private ILogger logger;
    private static String TAG = "NavigationActivity";
    private boolean navigatingToAnotherActivity = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger = NavigationApplication.getLogger();
        if (isFinishing()) {
            return;
        }

        if (navigator == null || navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            navigator = new Navigator(this,
                    new ChildControllersRegistry(),
                    new ModalStack(this),
                    new OverlayManager(),
                    new RootPresenter(this),
                    logger
            );
            addDefaultSplashLayout();
            navigator.bindViews();
            getReactGateway().onActivityCreated(this);
        }

        getApplication().registerActivityLifecycleCallbacks(lifecycleCallback);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getReactGateway().onConfigurationChanged(this, newConfig);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        logger.log(Log.INFO, TAG, "onPostCreate PIPMode " + navigator.getPipMode());
        if (navigator.getPipMode() == PIPStates.NOT_STARTED) {
            navigator.setContentLayout(findViewById(android.R.id.content));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatingToAnotherActivity = false;
        logger.log(Log.INFO, TAG, "onResume PIPMode " + navigator.getPipMode());
        if (navigator.getPipMode() == PIPStates.NOT_STARTED) {
            getReactGateway().onActivityResumed(this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        logger.log(Log.INFO, TAG, "onNewIntent PIPMode " + navigator.getPipMode());
        if (!getReactGateway().onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger.log(Log.INFO, TAG, "onPause PIPMode " + navigator.getPipMode());
        if (navigator.getPipMode() == PIPStates.NOT_STARTED) {
            getReactGateway().onActivityPaused(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.log(Log.INFO, TAG, "onDestroy");
        if (navigator != null) {
            logger.log(Log.INFO, TAG, "onDestroy PIPMode " + navigator.getPipMode());
            if (navigator.getPipMode() == PIPStates.NATIVE_MOUNTED) {
                navigator.updatePIPState(PIPStates.UNMOUNT_START);
            }
            navigator.destroy();
            getReactGateway().onActivityDestroyed(this);
        }
        getApplication().unregisterActivityLifecycleCallbacks(lifecycleCallback);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        logger.log(Log.INFO, TAG, "onUserLeaveHint shouldSwitchToPIP " + navigator.shouldSwitchToPIPonHomePress() + "  PIPMode " + navigator.getPipMode());
        if (!navigatingToAnotherActivity && navigator.shouldSwitchToPIPonHomePress() && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && canEnterPiPMode()) {
            navigator.updatePIPState(PIPStates.NATIVE_MOUNT_START);
            try {
                enterPictureInPictureMode(navigator.getPictureInPictureParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!navigatingToAnotherActivity && navigator.shouldSwitchToPIPonHomePress() &&
                (navigator.getPipMode() == PIPStates.CUSTOM_MOUNTED || navigator.getPipMode() == PIPStates.CUSTOM_COMPACT)) {
            navigator.updatePIPState(PIPStates.UNMOUNT_START);
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        logger.log(Log.VERBOSE, TAG, "onPictureInPictureModeChanged isInPictureInPictureMode " + isInPictureInPictureMode + "  PIPMode " + navigator.getPipMode() + " isFinishing " + isFinishing());
        if (isInPictureInPictureMode) {
            navigator.updatePIPState(PIPStates.NATIVE_MOUNTED);
        } else if (isFinishing()) {
            navigator.updatePIPState(PIPStates.UNMOUNT_START);
        } else {
            navigator.updatePIPState(PIPStates.CUSTOM_MOUNTED);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        logger.log(Log.VERBOSE, TAG, "invokeDefaultOnBackPressed  PIPMode " + navigator.getPipMode());
        if (isDestroyed())
            return;
        if (navigator.shouldSwitchToPIPonBackPress()) {
            navigator.updatePIPState(PIPStates.MOUNT_START);
        } else if (!navigator.handleBack(new CommandListenerAdapter())) {
            try {
                super.onBackPressed();
                handleExit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        logger.log(Log.VERBOSE, TAG, "onStop PIPMode " + navigator.getPipMode());
        if (navigator.getPipMode() == PIPStates.NATIVE_MOUNTED && NavigationActivity.this.anotherActivityCount <= 0) {
            finish();
        } else if (navigator.getPipMode() == PIPStates.NATIVE_MOUNT_START) {
            navigator.resetPIP();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logger.log(Log.VERBOSE, TAG, "onStop PIPMode " + navigator.getPipMode());
        getReactGateway().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        logger.log(Log.VERBOSE, TAG, "onBackPressed PIPMode " + navigator.getPipMode());
        getReactGateway().onBackPressed();
    }

    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        return getReactGateway().onKeyUp(keyCode) || super.onKeyUp(keyCode, event);
    }

    public void handleExit() {
        logger.log(Log.INFO, TAG, "handleExit");
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
        logger.log(Log.VERBOSE, TAG, "onReload PIPMode " + navigator.getPipMode());
        if (navigator.getPipMode() != PIPStates.NATIVE_MOUNTED) {
            navigator.destroyViews();
        }
    }

    protected void addDefaultSplashLayout() {
        View view = new View(this);
        view.setBackgroundColor(Color.WHITE);
        setContentView(view);
    }

    @Override
    public void startActivity(Intent intent) {
        navigatingToAnotherActivity = true;
        super.startActivity(intent);
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
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        return (AppOpsManager.MODE_ALLOWED == appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, android.os.Process.myUid(), getPackageName()));
    }
}