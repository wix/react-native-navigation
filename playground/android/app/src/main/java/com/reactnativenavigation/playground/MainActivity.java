package com.reactnativenavigation.playground;

import android.content.Intent;

import com.reactnativenavigation.NavigationActivity;

public class MainActivity extends NavigationActivity {
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        dismissSystemAlertsToPreventDetoxFromTimingOut(hasFocus);
    }

    private void dismissSystemAlertsToPreventDetoxFromTimingOut(boolean hasFocus) {
        if (! hasFocus) {
            sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }
}
