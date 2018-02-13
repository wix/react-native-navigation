package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.parsers.BoolParser;

import org.json.JSONObject;

public class BottomTabsOptions implements DEFAULT_VALUES {

	public static BottomTabsOptions parse(JSONObject json) {
		BottomTabsOptions options = new BottomTabsOptions();
		if (json == null) return options;

        options.color = ColorParser.parse(json, "tabColor");
        options.selectedColor = ColorParser.parse(json, "selectedTabColor");
        options.currentTabId = TextParser.parse(json, "currentTabId");
		options.currentTabIndex = json.optInt("currentTabIndex", NO_INT_VALUE);
		options.visible = BoolParser.parse(json,"visible");
		options.animateHide = BoolParser.parse(json,"animateHide");
        options.testId = TextParser.parse(json, "testID");

		return options;
	}

    public Color color = new NullColor();
    private Color selectedColor = new NullColor();
	Bool visible = new NullBool();
	Bool animateHide = new NullBool();
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
		if (other.visible.hasValue()) {
			visible = other.visible;
		}
		if (other.animateHide.hasValue()) {
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
        if (!visible.hasValue()) {
            visible = defaultOptions.visible;
        }
        if (!animateHide.hasValue()) {
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
