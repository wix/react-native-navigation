package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullFraction;
import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.params.TitleDisplayMode;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.FractionParser;
import com.reactnativenavigation.options.parsers.NumberParser;
import com.reactnativenavigation.options.parsers.TextParser;

import org.json.JSONObject;

public class BottomTabsOptions {

    public static BottomTabsOptions parse(Context context, JSONObject json) {
        BottomTabsOptions options = new BottomTabsOptions();
        if (json == null) return options;

        options.backgroundColor = ThemeColour.parse(context, json.optJSONObject("backgroundColor"));
        options.currentTabId = TextParser.parse(json, "currentTabId");
        options.currentTabIndex = NumberParser.parse(json, "currentTabIndex");
        options.hideOnScroll = BoolParser.parse(json, "hideOnScroll");
        options.visible = BoolParser.parse(json, "visible");
        options.drawBehind = BoolParser.parse(json, "drawBehind");
        options.preferLargeIcons = BoolParser.parse(json, "preferLargeIcons");
        options.animate = BoolParser.parse(json, "animate");
        options.animateTabSelection = BoolParser.parse(json, "animateTabSelection");
        options.elevation = FractionParser.parse(json, "elevation");
        options.testId = TextParser.parse(json, "testID");
        options.titleDisplayMode = TitleDisplayMode.fromString(json.optString("titleDisplayMode"));
        options.tabsAttachMode = TabsAttachMode.fromString(json.optString("tabsAttachMode"));
        options.borderColor = ThemeColour.parse(context, json.optJSONObject("borderColor"));
        options.borderWidth = FractionParser.parse(json, "borderWidth");
        options.shadowOptions = ShadowOptionsKt.parseShadowOptions(context, json.optJSONObject("shadow"));
        return options;
    }

    public ThemeColour backgroundColor = new NullThemeColour();
    public Bool hideOnScroll = new NullBool();
    public Bool visible = new NullBool();
    public Bool drawBehind = new NullBool();
    public Bool animate = new NullBool();
    public Bool animateTabSelection = new NullBool();
    public Bool preferLargeIcons = new NullBool();
    public Number currentTabIndex = new NullNumber();
    public Fraction elevation = new NullFraction();
    public Text currentTabId = new NullText();
    public Text testId = new NullText();
    public TitleDisplayMode titleDisplayMode = TitleDisplayMode.UNDEFINED;
    public TabsAttachMode tabsAttachMode = TabsAttachMode.UNDEFINED;
    public ThemeColour borderColor = new NullThemeColour();
    public Fraction borderWidth = new NullFraction();
    public ShadowOptions shadowOptions = NullShadowOptions.INSTANCE;

    void mergeWith(final BottomTabsOptions other) {
        if (other.currentTabId.hasValue()) currentTabId = other.currentTabId;
        if (other.currentTabIndex.hasValue()) currentTabIndex = other.currentTabIndex;
        if (other.hideOnScroll.hasValue()) hideOnScroll = other.hideOnScroll;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
        if (other.animate.hasValue()) animate = other.animate;
        if (other.animateTabSelection.hasValue()) animateTabSelection = other.animateTabSelection;
        if (other.preferLargeIcons.hasValue()) preferLargeIcons = other.preferLargeIcons;
        if (other.elevation.hasValue()) elevation = other.elevation;
        if (other.testId.hasValue()) testId = other.testId;
        if (other.titleDisplayMode.hasValue()) titleDisplayMode = other.titleDisplayMode;
        if (other.tabsAttachMode.hasValue()) tabsAttachMode = other.tabsAttachMode;
        if (other.borderWidth.hasValue()) borderWidth = other.borderWidth;
        if (other.shadowOptions.hasValue()) shadowOptions = shadowOptions.copy().mergeWith(other.shadowOptions);
        if (other.borderColor.hasValue()) borderColor = other.borderColor;
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor;

    }

    void mergeWithDefault(final BottomTabsOptions defaultOptions) {
        if (!borderColor.hasValue()) borderColor = defaultOptions.borderColor;
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor;
        if (!currentTabId.hasValue()) currentTabId = defaultOptions.currentTabId;
        if (!currentTabIndex.hasValue()) currentTabIndex = defaultOptions.currentTabIndex;
        if (!hideOnScroll.hasValue()) hideOnScroll = defaultOptions.hideOnScroll;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
        if (!animate.hasValue()) animate = defaultOptions.animate;
        if (!animateTabSelection.hasValue()) animateTabSelection = defaultOptions.animateTabSelection;
        if (!preferLargeIcons.hasValue()) preferLargeIcons = defaultOptions.preferLargeIcons;
        if (!elevation.hasValue()) elevation = defaultOptions.elevation;
        if (!titleDisplayMode.hasValue()) titleDisplayMode = defaultOptions.titleDisplayMode;
        if (!tabsAttachMode.hasValue()) tabsAttachMode = defaultOptions.tabsAttachMode;
        if (!borderWidth.hasValue()) borderWidth = defaultOptions.borderWidth;
        if (!shadowOptions.hasValue())
            shadowOptions = shadowOptions.copy().mergeWithDefaults(defaultOptions.shadowOptions);
    }

    public boolean isHiddenOrDrawBehind() {
        return visible.isFalse() || drawBehind.isTrue();
    }

    public void clearOneTimeOptions() {
        currentTabId = new NullText();
        currentTabIndex = new NullNumber();
    }
}
