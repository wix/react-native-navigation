package com.reactnativenavigation.parse;


import com.reactnativenavigation.parse.Options.BooleanOptions;

import org.json.JSONObject;

public class BottomTabsOptions implements DEFAULT_VALUES {

	public static BottomTabsOptions parse(JSONObject json) {
		BottomTabsOptions options = new BottomTabsOptions();
		if (json == null) return options;

		options.currentTabId = TextParser.parse(json, "currentTabId");
		options.currentTabIndex = json.optInt("currentTabIndex", NO_INT_VALUE);
		options.tabBadge = json.optInt("tabBadge", NO_INT_VALUE);
		options.hidden = BooleanOptions.parse(json.optString("hidden"));
		options.animateHide = BooleanOptions.parse(json.optString("animateHide"));

		return options;
	}

	int tabBadge = NO_INT_VALUE;
	BooleanOptions hidden = BooleanOptions.False;
	BooleanOptions animateHide = BooleanOptions.False;
	public int currentTabIndex = NO_INT_VALUE;
	public Text currentTabId = new NullText();

	void mergeWith(final BottomTabsOptions other) {
		if (other.currentTabId.hasValue()) {
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
        if (!currentTabId.hasValue()) {
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
