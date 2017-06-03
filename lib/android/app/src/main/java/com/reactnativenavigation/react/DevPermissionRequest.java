package com.reactnativenavigation.react;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.common.ReactConstants;

public class DevPermissionRequest {

	private final boolean isDebug;

	public DevPermissionRequest(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean shouldAskPermission(Activity activity) {
		return isDebug &&
				Build.VERSION.SDK_INT >= 23 &&
				!Settings.canDrawOverlays(activity);
	}

	@TargetApi(23)
	public void askPermission(Activity activity) {
		if (shouldAskPermission(activity)) {
			Intent serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
			serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(serviceIntent);
			String msg = "Overlay permissions needs to be granted in order for react native apps to run in dev mode";
			Log.w(ReactConstants.TAG, "======================================\n\n");
			Log.w(ReactConstants.TAG, msg);
			Log.w(ReactConstants.TAG, "\n\n======================================");
			Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
		}
	}
}
