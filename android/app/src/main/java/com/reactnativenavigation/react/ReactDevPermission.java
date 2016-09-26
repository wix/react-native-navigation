package com.reactnativenavigation.react;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.common.ReactConstants;
import com.reactnativenavigation.NavigationApplication;

public class ReactDevPermission {

    public static boolean shouldAskPermission() {
        return NavigationApplication.instance.isDebug() &&
                Build.VERSION.SDK_INT >= 23 &&
                !Settings.canDrawOverlays(NavigationApplication.instance);
    }

    @TargetApi(23)
    public static void askPermission(Context context) {
        if (shouldAskPermission()) {
            Intent serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            context.startActivity(serviceIntent);
            String msg = "Overlay permissions needs to be granted in order for react native apps to run in dev mode";
            Log.w(ReactConstants.TAG, "======================================\n\n");
            Log.w(ReactConstants.TAG, msg);
            Log.w(ReactConstants.TAG, "\n\n======================================");
            for (int i = 0; i < 5; i++) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
