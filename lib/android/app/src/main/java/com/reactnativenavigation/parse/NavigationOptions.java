package com.reactnativenavigation.parse;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import org.json.JSONObject;

public class NavigationOptions {

	@NonNull
	public static NavigationOptions parse(JSONObject json) {
		NavigationOptions result = new NavigationOptions();
		if (json == null) return result;

		result.title = json.optString("title");
		result.topBarBackgroundColor = json.optInt("topBarBackgroundColor");
		result.topBarTextColor = json.optInt("topBarTextColor");
		result.topBarTextFontSize = (float) json.optDouble("topBarTextFontSize");

		return result;
	}

	public String title = "";
	public int topBarBackgroundColor = 0;
	@ColorInt
	public int topBarTextColor;
	public float topBarTextFontSize;

	public void mergeWith(final NavigationOptions other) {
		title = other.title;
		topBarBackgroundColor = other.topBarBackgroundColor;
		topBarTextColor = other.topBarTextColor;
		topBarTextFontSize = other.topBarTextFontSize;
	}
}
