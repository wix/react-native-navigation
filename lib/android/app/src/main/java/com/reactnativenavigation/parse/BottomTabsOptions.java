package com.reactnativenavigation.parse;


import com.reactnativenavigation.parse.Options.BooleanOptions;

import org.json.JSONObject;

public class BottomTabsOptions implements DEFAULT_VALUES {

	public static BottomTabsOptions parse(JSONObject json) {
		BottomTabsOptions options = new BottomTabsOptions();
		if (json == null) return options;

        options.color = ColorParser.parse(json, "tabColor");
        options.selectedColor = ColorParser.parse(json, "selectedTabColor");
        options.currentTabId = TextParser.parse(json, "currentTabId");
		options.currentTabIndex = json.optInt("currentTabIndex", NO_INT_VALUE);
		options.visible = BooleanOptions.parse(json.optString("visible"));
		options.animateHide = BooleanOptions.parse(json.optString("animateHide"));
        options.testId = TextParser.parse(json, "testID");

		return options;
	}

    public Color color = new NullColor();
    public Color selectedColor = new NullColor();
	BooleanOptions visible = BooleanOptions.False;
	BooleanOptions animateHide = BooleanOptions.False;
	public int currentTabIndex = NO_INT_VALUE;
	public Text currentTabId = new NullText();
    public Text testId = new NullText();

	void mergeWith(final BottomTabsOptions other) {
		if (other.currentTabId.hasValue()) {
			currentTabId = other.currentTabId;
		}
		if (NO_INT_VALUE != other.currentTabIndex) {
            currentTabIndex = other.currentTabIndex;
		}
		if (other.visible != BooleanOptions.NoValue) {
			visible = other.visible;
		}
		if (other.animateHide != BooleanOptions.NoValue) {
			animateHide = other.animateHide;
		}
        if (other.color.hasValue()) {
            color = other.color;
        }
        if (other.selectedColor.hasValue()) {
            selectedColor = other.selectedColor;
        }
    }

    void mergeWithDefault(final BottomTabsOptions defaultOptions) {
        if (!currentTabId.hasValue()) {
            currentTabId = defaultOptions.currentTabId;
        }
        if (NO_INT_VALUE == currentTabIndex) {
            currentTabIndex = defaultOptions.currentTabIndex;
        }
        if (visible == BooleanOptions.NoValue) {
            visible = defaultOptions.visible;
        }
        if (animateHide == BooleanOptions.NoValue) {
            animateHide = defaultOptions.animateHide;
        }
        if (!color.hasValue()) {
            color = defaultOptions.color;
        }
        if (!selectedColor.hasValue()) {
            selectedColor = defaultOptions.selectedColor;
        }
    }
}
