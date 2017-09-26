package com.reactnativenavigation.parse;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

public class NavigationOptions {

	private static final int DEFAULT_COLOR = Color.WHITE;

	public NavigationOptions(JSONObject json) {
		parse(json);
	}

	public String title;
	@ColorInt
	public int topBarBackgroundColor = DEFAULT_COLOR;
	@ColorInt
	public int topBarTextColor;
	public float topBarTextFontSize;
	public String topBarTextFontFamily;
	public boolean topBarHidden;

	public void mergeWith(JSONObject json) {
		parse(json);
	}

	private void parse(JSONObject json) {
		if (json == null) return;

		this.title = json.optString("title", title);
		this.topBarBackgroundColor = json.optInt("topBarBackgroundColor", topBarBackgroundColor);
		this.topBarTextColor = json.optInt("topBarTextColor", topBarTextColor);
		this.topBarTextFontSize = (float) json.optDouble("topBarTextFontSize", topBarTextFontSize);
		this.topBarTextFontFamily = json.optString("topBarTextFontFamily", topBarTextFontFamily);
		this.topBarHidden = json.optBoolean("topBarHidden", topBarHidden);
	}
}
