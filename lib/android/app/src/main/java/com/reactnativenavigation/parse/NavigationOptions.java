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
		result.topBarTextFontFamily = json.optString("topBarTextFontFamily");

		return result;
	}

	public String title = "";
	public int topBarBackgroundColor = 0;
	@ColorInt
	public int topBarTextColor;
	public float topBarTextFontSize;
	public String topBarTextFontFamily;

	public void mergeWith(final NavigationOptions other) {
		title = other.title;
		topBarBackgroundColor = other.topBarBackgroundColor;
		topBarTextColor = other.topBarTextColor;
		topBarTextFontSize = other.topBarTextFontSize;
		topBarTextFontFamily = other.topBarTextFontFamily;
	}
}
