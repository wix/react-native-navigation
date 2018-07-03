package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

import com.facebook.react.ReactInstanceManager;

public class JsDevReloadHandler {
    public interface ReloadListener {
        void onReload();
    }

	private static final String RELOAD_BROADCAST = "com.reactnativenavigation.broadcast.RELOAD";
	private final BroadcastReceiver reloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			reloadReactNative();
		}
	};
	private final ReactInstanceManager reactInstanceManager;
	private long firstRTimestamp = 0;
    private ReloadListener reloadListener;

    JsDevReloadHandler(final ReactInstanceManager reactInstanceManager) {
		this.reactInstanceManager = reactInstanceManager;
	}

    public void setReloadListener(ReloadListener listener) {
        reloadListener = listener;
    }

    public void removeReloadListener(ReloadListener listener) {
        if (reloadListener == listener) {
            reloadListener = null;
        }
    }

	public void onActivityResumed(Activity activity) {
		activity.registerReceiver(reloadReceiver, new IntentFilter(RELOAD_BROADCAST));
	}

	public void onActivityPaused(Activity activity) {
		activity.unregisterReceiver(reloadReceiver);
	}

	public boolean onKeyUp(int keyCode) {
		if (!reactInstanceManager.getDevSupportManager().getDevSupportEnabled()) {
			return false;
		}

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			reactInstanceManager.getDevSupportManager().showDevOptionsDialog();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_R) {
			if (lessThan500MillisSinceLastR()) {
				reloadReactNative();
				return true;
			}
			firstRTimestamp = System.currentTimeMillis();
		}
		return false;
	}

	private boolean lessThan500MillisSinceLastR() {
		return firstRTimestamp != 0 && System.currentTimeMillis() - firstRTimestamp < 1000;
	}

	private void reloadReactNative() {
        reloadListener.onReload();
		reactInstanceManager.getDevSupportManager().handleReloadJS();
	}
}
