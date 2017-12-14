package com.reactnativenavigation;

import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import com.facebook.react.modules.core.*;
import com.reactnativenavigation.viewcontrollers.*;

public class NavigationActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
	private Navigator navigator;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app().getReactGateway().onActivityCreated(this);
		navigator = new Navigator(this);
		setContentView(navigator.getView());
	}

	@Override
	protected void onResume() {
		super.onResume();
		app().getReactGateway().onActivityResumed(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		app().getReactGateway().onActivityPaused(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		navigator.destroy();
		app().getReactGateway().onActivityDestroyed(this);
	}

	@Override
	public void invokeDefaultOnBackPressed() {
		if (!navigator.handleBack()) {
			super.onBackPressed();
		}
	}

	@Override
	public void onBackPressed() {
		app().getReactGateway().onBackPressed();
	}

	@Override
	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		return app().getReactGateway().onKeyUp(keyCode) || super.onKeyUp(keyCode, event);
	}

	private NavigationApplication app() {
		return (NavigationApplication) getApplication();
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void toast(final String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}
