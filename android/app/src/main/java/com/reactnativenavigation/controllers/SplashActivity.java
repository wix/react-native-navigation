package com.reactnativenavigation.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.react.ReactDevPermission;

public abstract class SplashActivity extends AppCompatActivity {
    public static boolean isResumed = false;

    public static void start(Activity activity) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        if (intent == null) return;
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSplashLayout();
        IntentDataHandler.saveIntentData(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;

        if (NavigationApplication.instance.getReactGateway().hasStartedCreatingContext()) {
            NavigationApplication.instance.getEventEmitter().sendAppLaunchedEvent();
            if (NavigationApplication.instance.clearHostOnActivityDestroy()) {
                overridePendingTransition(0, 0);
                finish();
            }
            return;
        }

        if (ReactDevPermission.shouldAskPermission()) {
            ReactDevPermission.askPermission(this);
            return;
        }

        if (NavigationApplication.instance.isReactContextInitialized()) {
            NavigationApplication.instance.getEventEmitter().sendAppLaunchedEvent();
            return;
        }

        // TODO I'm starting to think this entire flow is incorrect and should be done in Application
        NavigationApplication.instance.startReactContextOnceInBackgroundAndExecuteJS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    private void setSplashLayout() {
        final int splashLayout = getSplashLayout();
        if (splashLayout > 0) {
            setContentView(splashLayout);
        } else {
            setContentView(createSplashLayout());
        }
    }

    /**
     * @return xml layout res id
     */
    @LayoutRes
    public int getSplashLayout() {
        return 0;
    }

    /**
     * @return the layout you would like to show while react's js context loads
     */
    public View createSplashLayout() {
        View view = new View(this);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }
}
