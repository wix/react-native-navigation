package com.reactnativenavigation.layout;

import android.support.annotation.NonNull;

import org.json.JSONObject;

public class NavigationOptions {

	@NonNull
	public static NavigationOptions parse(JSONObject json) {
		if (json == null) {
			return new NavigationOptions();
		}

		return new NavigationOptions(
				json.optString("title"),
				json.has("topBarHidden") ? json.optBoolean("topBarHidden") : null
		);
	}

	@NonNull
	static NavigationOptions merge(NavigationOptions main, NavigationOptions update) {
		return new NavigationOptions(
				update.title == null ? main.title : update.title,
				update.topBarHidden == null ? main.topBarHidden : update.topBarHidden
		);
	}

	public final String title;
	public final Boolean topBarHidden;

	public NavigationOptions() {
		this("", Boolean.FALSE);
	}

	private NavigationOptions(String title, Boolean topBarHidden) {
		this.title = title;
		this.topBarHidden = topBarHidden;
	}

}
