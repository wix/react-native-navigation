package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class BottomTabsOptions implements DEFAULT_VALUES {

	public static BottomTabsOptions parse(JSONObject json) {
		BottomTabsOptions options = new BottomTabsOptions();
		if (json == null) return options;

		options.currentTabId = json.optString("currentTabId", NO_VALUE);
		options.currentTabIndex = json.optInt("currentTabIndex", NO_INT_VALUE);
		options.tabBadge = json.optInt("tabBadge", NO_INT_VALUE);
		options.hidden = NavigationOptions.BooleanOptions.parse(json.optString("hidden"));
		options.animateHide = NavigationOptions.BooleanOptions.parse(json.optString("animateHide"));

		return options;
	}

	public int tabBadge = NO_INT_VALUE;
	public NavigationOptions.BooleanOptions hidden = NavigationOptions.BooleanOptions.False;
	public NavigationOptions.BooleanOptions animateHide = NavigationOptions.BooleanOptions.False;
	public int currentTabIndex;
	public String currentTabId;

	void mergeWith(final BottomTabsOptions other) {
		if (!NO_VALUE.equals(other.currentTabId)) {
			currentTabId = other.currentTabId;
		}
		if(NO_INT_VALUE != other.currentTabIndex) {
			currentTabId = other.currentTabId;
		}
		if(NO_INT_VALUE != other.tabBadge) {
			tabBadge = other.tabBadge;
		}
		if (other.hidden != NavigationOptions.BooleanOptions.NoValue) {
			hidden = other.hidden;
		}
		if (other.animateHide != NavigationOptions.BooleanOptions.NoValue) {
			animateHide = other.animateHide;
		}
	}
}
