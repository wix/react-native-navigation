package com.reactnativenavigation.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;

public class ActivityCallbacks {
    WeakReference<Activity> activityReference;
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    public void onActivityStarted(Activity activity) {
        activityReference = new WeakReference(activity);
    }

    public void onActivityResumed(Activity activity) {

    }

    public void onActivityPaused(Activity activity) {

    }

    public void onActivityStopped(Activity activity) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onActivityDestroyed(Activity activity) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    public void onNewIntent(Intent intent) {

    }

    public void onConfigurationChanged(Configuration newConfig) {
        Intent intent = new Intent("onConfigurationChanged");
        intent.putExtra("newConfig", newConfig);
 
        activityReference.get().sendBroadcast(intent);
    }

    public void onKeyUp(int keyCode, KeyEvent event) {

    }

}
