package com.reactnativenavigation.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactRootView;


public class CustomDialog extends Dialog {

	private View container;

	public CustomDialog(@NonNull Context context, @NonNull View container) {
		super(context);

		this.container = container;
		init();
	}

	private void init() {
		addContentView(container, new ViewGroup.LayoutParams(500, 500));
	}
}
