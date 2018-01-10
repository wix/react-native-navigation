package com.reactnativenavigation.parse;


import com.reactnativenavigation.parse.NavigationOptions.BooleanOptions;

import org.json.JSONObject;

public class BottomTabsOptions implements DEFAULT_VALUES {

	public static BottomTabsOptions parse(JSONObject json) {
		BottomTabsOptions options = new BottomTabsOptions();
		if (json == null) return options;

		options.currentTabId = json.optString("currentTabId", NO_VALUE);
		options.currentTabIndex = json.optInt("currentTabIndex", NO_INT_VALUE);
		options.tabBadge = json.optInt("tabBadge", NO_INT_VALUE);
		options.hidden = BooleanOptions.parse(json.optString("hidden"));
		options.animateHide = BooleanOptions.parse(json.optString("animateHide"));

		return options;
	}

	public int tabBadge = NO_INT_VALUE;
	public BooleanOptions hidden = BooleanOptions.False;
	public BooleanOptions animateHide = BooleanOptions.False;
	public int currentTabIndex = NO_INT_VALUE;
	public String currentTabId = NO_VALUE;

	void mergeWith(final BottomTabsOptions other) {
		if (!NO_VALUE.equals(other.currentTabId)) {
			currentTabId = other.currentTabId;
		}
		if (NO_INT_VALUE != other.currentTabIndex) {
            currentTabIndex = other.currentTabIndex;
		}
		if (NO_INT_VALUE != other.tabBadge) {
			tabBadge = other.tabBadge;
		}
		if (other.hidden != BooleanOptions.NoValue) {
			hidden = other.hidden;
		}
		if (other.animateHide != BooleanOptions.NoValue) {
			animateHide = other.animateHide;
		}
	}

    void mergeWithDefault(final BottomTabsOptions defaultOptions) {
        if (NO_VALUE.equals(currentTabId)) {
            currentTabId = defaultOptions.currentTabId;
        }
        if (NO_INT_VALUE == currentTabIndex) {
            currentTabIndex = defaultOptions.currentTabIndex;
        }
        if (NO_INT_VALUE == tabBadge) {
            tabBadge = defaultOptions.tabBadge;
        }
        if (hidden == BooleanOptions.NoValue) {
            hidden = defaultOptions.hidden;
        }
        if (animateHide == BooleanOptions.NoValue) {
            animateHide = defaultOptions.animateHide;
        }
    }
}
