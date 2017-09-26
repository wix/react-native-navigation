package com.reactnativenavigation.parse;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

public class NavigationOptions {

	private static final String DEFAULT_STRING = "default_string";
	private static final int DEFAULT_INT = Integer.MIN_VALUE;
	private static final float DEFAULT_FLOAT = Float.MIN_VALUE;
	private static final int DEFAULT_COLOR = Color.WHITE;

	@NonNull
	public static NavigationOptions parse(JSONObject json) {
		NavigationOptions result = new NavigationOptions();
		if (json == null) return result;

		result.title = json.optString("title", DEFAULT_STRING);
		result.topBarBackgroundColor = json.optInt("topBarBackgroundColor", DEFAULT_COLOR);
		result.topBarTextColor = json.optInt("topBarTextColor", DEFAULT_INT);
		result.topBarTextFontSize = (float) json.optDouble("topBarTextFontSize", DEFAULT_FLOAT);
		result.topBarTextFontFamily = json.optString("topBarTextFontFamily", DEFAULT_STRING);
		result.topBarHidden = json.optBoolean("topBarHidden");

		return result;
	}

	public String title;
	@ColorInt
	public int topBarBackgroundColor = DEFAULT_COLOR;
	@ColorInt
	public int topBarTextColor;
	public float topBarTextFontSize;
	public String topBarTextFontFamily;
	public boolean topBarHidden;

	public void mergeWith(final NavigationOptions other) {
		if (!other.title.equals(DEFAULT_STRING)) title = other.title;
		if (other.topBarBackgroundColor != DEFAULT_COLOR)
			topBarBackgroundColor = other.topBarBackgroundColor;
		if (other.topBarTextColor != DEFAULT_INT)
			topBarTextColor = other.topBarTextColor;
		if (other.topBarTextFontSize != DEFAULT_FLOAT)
			topBarTextFontSize = other.topBarTextFontSize;
		if (!other.topBarTextFontFamily.equals(DEFAULT_STRING))
			topBarTextFontFamily = other.topBarTextFontFamily;
		topBarHidden = other.topBarHidden;
	}
}
