package com.reactnativenavigation.parse;


import android.support.annotation.ColorInt;

import org.json.JSONObject;

public class TopBarOptions implements DEFAULT_VALUES {

	public static TopBarOptions parse(JSONObject json) {
		TopBarOptions options = new TopBarOptions();
		if (json == null) return options;

		options.title = json.optString("title", NO_VALUE);
		options.backgroundColor = json.optInt("backgroundColor", NO_COLOR_VALUE);
		options.textColor = json.optInt("textColor", NO_INT_VALUE);
		options.textFontSize = (float) json.optDouble("textFontSize", NO_FLOAT_VALUE);
		options.textFontFamily = json.optString("textFontFamily", NO_VALUE);
		options.hidden = NavigationOptions.BooleanOptions.parse(json.optString("hidden"));
		options.animateHide = NavigationOptions.BooleanOptions.parse(json.optString("animateHide"));

		return options;
	}

	public String title = NO_VALUE;
	@ColorInt
	public int backgroundColor = NO_COLOR_VALUE;
	@ColorInt
	public int textColor = NO_COLOR_VALUE;
	public float textFontSize = NO_FLOAT_VALUE;
	public String textFontFamily = NO_VALUE;
	public NavigationOptions.BooleanOptions hidden = NavigationOptions.BooleanOptions.False;
	public NavigationOptions.BooleanOptions animateHide = NavigationOptions.BooleanOptions.False;

	void mergeWith(final TopBarOptions other) {
		if (!NO_VALUE.equals(other.title)) title = other.title;
		if (other.backgroundColor != NO_COLOR_VALUE)
			backgroundColor = other.backgroundColor;
		if (other.textColor != NO_INT_VALUE)
			textColor = other.textColor;
		if (other.textFontSize != NO_FLOAT_VALUE)
			textFontSize = other.textFontSize;
		if (!NO_VALUE.equals(other.textFontFamily))
			textFontFamily = other.textFontFamily;
		if (other.hidden != NavigationOptions.BooleanOptions.NoValue) {
			hidden = other.hidden;
		}
		if (other.animateHide != NavigationOptions.BooleanOptions.NoValue) {
			animateHide = other.animateHide;
		}
	}
}
