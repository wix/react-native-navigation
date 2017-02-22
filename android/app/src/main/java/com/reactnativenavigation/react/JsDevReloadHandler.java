package com.reactnativenavigation.react;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.NavigationApplication;

class JsDevReloadHandler {

    private static boolean shouldRefreshOnRR = false;
    private final BroadcastReceiver reloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            reload();
        }
    };

    void onResumeActivity() {
        if (getReactInstanceManager().getDevSupportManager().getDevSupportEnabled()) {
            NavigationApplication.instance.registerReceiver(reloadReceiver, new IntentFilter("react.native.RELOAD"));
        }
    }

    void onPauseActivity() {
        if (getReactInstanceManager().getDevSupportManager().getDevSupportEnabled()) {
            NavigationApplication.instance.unregisterReceiver(reloadReceiver);
        }
    }

    boolean onKeyUp(View currentFocus, int keyCode) {
        ReactInstanceManager reactInstanceManager = getReactInstanceManager();

        if (reactInstanceManager != null &&
                reactInstanceManager.getDevSupportManager().getDevSupportEnabled()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                reactInstanceManager.showDevOptionsDialog();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_R && !(currentFocus instanceof EditText)) {
                // Enable double-tap-R-to-reload
                if (shouldRefreshOnRR) {
                    reload();
                    return true;
                } else {
                    shouldRefreshOnRR = true;
                    NavigationApplication.instance.runOnMainThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    shouldRefreshOnRR = false;
                                }
                            },
                            500);
                }
            }
        }
        return false;
    }

    private void reload() {
        getReactInstanceManager().getDevSupportManager().handleReloadJS();
        shouldRefreshOnRR = false;
    }

    private ReactInstanceManager getReactInstanceManager() {
        return NavigationApplication
                .instance
                .getReactGateway()
                .getReactInstanceManager();
    }
}
