package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullFraction;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.params.TitleDisplayMode;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.NumberParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

public class BottomTabsOptions {

	public static BottomTabsOptions parse(JSONObject json) {
		BottomTabsOptions options = new BottomTabsOptions();
		if (json == null) return options;

        options.backgroundColor = ColorParser.parse(json, "backgroundColor");
        options.currentTabId = TextParser.parse(json, "currentTabId");
		options.currentTabIndex = NumberParser.parse(json,"currentTabIndex");
        options.hideOnScroll = BoolParser.parse(json, "hideOnScroll");
		options.visible = BoolParser.parse(json,"visible");
        options.drawBehind = BoolParser.parse(json, "drawBehind");
        options.preferLargeIcons = BoolParser.parse(json, "preferLargeIcons");
		options.animate = BoolParser.parse(json,"animate");
        options.elevation = FractionParser.parse(json, "elevation");
        options.testId = TextParser.parse(json, "testID");
        options.titleDisplayMode = TitleDisplayMode.fromString(json.optString("titleDisplayMode"));
        options.tabsAttachMode = TabsAttachMode.fromString(json.optString("tabsAttachMode"));

		return options;
	}

    public Colour backgroundColor = new NullColor();
    public Bool hideOnScroll = new NullBool();
	public Bool visible = new NullBool();
    public Bool drawBehind = new NullBool();
	public Bool animate = new NullBool();
    public Bool preferLargeIcons = new NullBool();
	public Number currentTabIndex = new NullNumber();
	public Fraction elevation = new NullFraction();
	public Text currentTabId = new NullText();
    public Text testId = new NullText();
    public TitleDisplayMode titleDisplayMode = TitleDisplayMode.UNDEFINED;
    public TabsAttachMode tabsAttachMode = TabsAttachMode.UNDEFINED;

	void mergeWith(final BottomTabsOptions other) {
		if (other.currentTabId.hasValue()) currentTabId = other.currentTabId;
		if (other.currentTabIndex.hasValue()) currentTabIndex = other.currentTabIndex;
        if (other.hideOnScroll.hasValue()) hideOnScroll = other.hideOnScroll;
		if (other.visible.hasValue()) visible = other.visible;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
		if (other.animate.hasValue()) animate = other.animate;
        if (other.preferLargeIcons.hasValue()) preferLargeIcons = other.preferLargeIcons;
        if (other.elevation.hasValue()) elevation = other.elevation;
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;
        if (other.testId.hasValue()) testId = other.testId;
        if (other.titleDisplayMode.hasValue()) titleDisplayMode = other.titleDisplayMode;
        if (other.tabsAttachMode.hasValue()) tabsAttachMode = other.tabsAttachMode;
    }

    void mergeWithDefault(final BottomTabsOptions defaultOptions) {
        if (!currentTabId.hasValue()) currentTabId = defaultOptions.currentTabId;
        if (!currentTabIndex.hasValue()) currentTabIndex = defaultOptions.currentTabIndex;
        if (!hideOnScroll.hasValue()) hideOnScroll = defaultOptions.hideOnScroll;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
        if (!animate.hasValue()) animate = defaultOptions.animate;
        if (!preferLargeIcons.hasValue()) preferLargeIcons = defaultOptions.preferLargeIcons;
        if (!elevation.hasValue()) elevation = defaultOptions.elevation;
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!titleDisplayMode.hasValue()) titleDisplayMode = defaultOptions.titleDisplayMode;
        if (!tabsAttachMode.hasValue()) tabsAttachMode = defaultOptions.tabsAttachMode;
    }

    public boolean isHiddenOrDrawBehind() {
        return visible.isFalse() || drawBehind.isTrue();
    }

    public void clearOneTimeOptions() {
        currentTabId = new NullText();
        currentTabIndex = new NullNumber();
    }
}
