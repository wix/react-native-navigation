package com.reactnativenavigation.layout;

import android.support.annotation.NonNull;

import org.json.JSONObject;

public class NavigationOptions {

	@NonNull
	public static NavigationOptions parse(JSONObject json) {
		NavigationOptions result = new NavigationOptions();
		if (json == null) return result;
		result.title = json.optString("title");
		return result;
	}

	public String title = "";
}
