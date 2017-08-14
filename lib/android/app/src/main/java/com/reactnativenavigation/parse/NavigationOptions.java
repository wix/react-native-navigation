package com.reactnativenavigation.parse;

import android.support.annotation.NonNull;

import org.json.JSONObject;

public class NavigationOptions {

	@NonNull
	public static NavigationOptions parse(JSONObject json) {
		NavigationOptions result = new NavigationOptions();
		if (json == null) return result;

		result.title = json.optString("title");
		result.topBarBackgroundColor = json.optInt("topBarBackgroundColor");

		return result;
	}

	public String title = "";
	public int topBarBackgroundColor = 0;

	public void mergeWith(final NavigationOptions other) {
		title = other.title;
		topBarBackgroundColor = other.topBarBackgroundColor;
	}
}
