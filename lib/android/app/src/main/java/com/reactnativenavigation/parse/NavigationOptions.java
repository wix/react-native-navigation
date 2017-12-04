package com.reactnativenavigation.parse;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import org.json.JSONObject;

public class NavigationOptions implements DEFAULT_VALUES {

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

		result.topBarOptions = TopBarOptions.parse(json.optJSONObject("topBar"));
		result.bottomTabsOptions = BottomTabsOptions.parse(json.optJSONObject("tabBar"));

		return result;
	}

	public TopBarOptions topBarOptions = new TopBarOptions();
	public BottomTabsOptions bottomTabsOptions = new BottomTabsOptions();

	public void mergeWith(final NavigationOptions other) {
		topBarOptions.mergeWith(other.topBarOptions);
		bottomTabsOptions.mergeWith(other.bottomTabsOptions);
	}
}
