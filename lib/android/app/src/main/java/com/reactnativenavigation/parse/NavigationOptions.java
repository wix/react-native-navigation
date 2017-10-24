package com.reactnativenavigation.parse;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import org.json.JSONObject;

public class NavigationOptions {

	private static final String NO_VALUE = "";
	private static final int NO_INT_VALUE = Integer.MIN_VALUE;
	private static final float NO_FLOAT_VALUE = Float.MIN_VALUE;
	private static final int NO_COLOR_VALUE = Color.TRANSPARENT;

	public enum BooleanOptions {
		True,
		False,
		NoValue;

		static BooleanOptions parse(String value) {
			if (value != null && !value.equals("")) {
				return Boolean.valueOf(value) ? True : False;
			}
			return NoValue;
		}
	}

	@NonNull
	public static NavigationOptions parse(JSONObject json) {
		NavigationOptions result = new NavigationOptions();
		if (json == null) return result;

		result.title = json.optString("title", NO_VALUE);
		result.topBarBackgroundColor = json.optInt("topBarBackgroundColor", NO_COLOR_VALUE);
		result.topBarTextColor = json.optInt("topBarTextColor", NO_INT_VALUE);
		result.topBarTextFontSize = (float) json.optDouble("topBarTextFontSize", NO_FLOAT_VALUE);
		result.topBarTextFontFamily = json.optString("topBarTextFontFamily", NO_VALUE);
		result.topBarHidden = BooleanOptions.parse(json.optString("topBarHidden"));
		result.animateTopBarHide = BooleanOptions.parse(json.optString("animateTopBarHide"));

		return result;
	}

	public String title = "";
	@ColorInt
	public int topBarBackgroundColor;
	@ColorInt
	public int topBarTextColor;
	public float topBarTextFontSize;
	public String topBarTextFontFamily;
	public BooleanOptions topBarHidden = BooleanOptions.False;
	public BooleanOptions animateTopBarHide = BooleanOptions.False;

	public void mergeWith(final NavigationOptions other) {
		if (!NO_VALUE.equals(other.title)) title = other.title;
		if (other.topBarBackgroundColor != NO_COLOR_VALUE)
			topBarBackgroundColor = other.topBarBackgroundColor;
		if (other.topBarTextColor != NO_INT_VALUE)
			topBarTextColor = other.topBarTextColor;
		if (other.topBarTextFontSize != NO_FLOAT_VALUE)
			topBarTextFontSize = other.topBarTextFontSize;
		if (!NO_VALUE.equals(other.topBarTextFontFamily))
			topBarTextFontFamily = other.topBarTextFontFamily;
		if (other.topBarHidden != BooleanOptions.NoValue) {
			topBarHidden = other.topBarHidden;
		}
		if (other.animateTopBarHide != BooleanOptions.NoValue) {
			animateTopBarHide = other.animateTopBarHide;
		}
	}
}
