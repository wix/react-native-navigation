package com.reactnativenavigation.react;

import android.view.KeyEvent;

import com.facebook.react.ReactInstanceManager;

public class JsDevReloadHandler {
	private final ReactInstanceManager reactInstanceManager;
	private long firstRTimestamp = 0;

	public JsDevReloadHandler(final ReactInstanceManager reactInstanceManager) {
		this.reactInstanceManager = reactInstanceManager;
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
				reactInstanceManager.getDevSupportManager().handleReloadJS();
				return true;
			}
			firstRTimestamp = System.currentTimeMillis();
		}
		return false;
	}

	private boolean lessThan500MillisSinceLastR() {
		return firstRTimestamp != 0 && System.currentTimeMillis() - firstRTimestamp < 1000;
	}
}
