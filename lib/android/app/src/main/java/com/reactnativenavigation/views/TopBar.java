package com.reactnativenavigation.views;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

public class TopBar extends AppBarLayout {
	private final Toolbar titleBar;

	public TopBar(final Activity context) {
		super(context);
		titleBar = new Toolbar(context);
		addView(titleBar);
	}

	public void setTitle(String title) {
		titleBar.setTitle(title);
	}

	public String getTitle() {
		return titleBar.getTitle() != null ? titleBar.getTitle().toString() : "";
	}
}
