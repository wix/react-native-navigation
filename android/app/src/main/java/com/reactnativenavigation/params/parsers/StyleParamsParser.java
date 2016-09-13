package com.reactnativenavigation.params.parsers;

import android.graphics.Color;
import android.os.Bundle;

import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.StyleParams;

public class StyleParamsParser {
    private Bundle params;

    public StyleParamsParser(Bundle params) {
        this.params = params;
    }

    public StyleParams parse() {
        StyleParams result = new StyleParams();
        if (params == null) {
            return result;
        }

        result.statusBarColor = getColor("statusBarColor", getDefaultStatusBarColor());

        result.topBarColor = getColor("topBarColor", getDefaultTopBarColor());
        result.titleBarHidden = getBoolean("titleBarHidden", getDefaultTopBarHidden());
        result.titleBarTitleColor = getColor("titleBarTitleColor", getDefaultTitleBarColor());
        result.titleBarSubtitleColor = getColor("titleBarSubtitleColor", getDefaultSubtitleBarColor());
        result.titleBarButtonColor = getColor("titleBarButtonColor", getTitleBarButtonColor());
        result.titleBarDisabledButtonColor = getColor("titleBarDisabledButtonColor", getTitleBarDisabledButtonColor());
        result.backButtonHidden = getBoolean("backButtonHidden", getDefaultBackButtonHidden());
        result.topTabsHidden = getBoolean("topTabsHidden", getDefaultTopTabsHidden());

        result.topTabTextColor = getColor("topTabTextColor", getDefaultTopTabTextColor());
        result.selectedTopTabTextColor = getColor("selectedTopTabTextColor", getDefaultSelectedTopTabTextColor());
        result.selectedTopTabIndicatorHeight = getInt("selectedTopTabIndicatorHeight", getDefaultSelectedTopTabIndicatorHeight());
        result.selectedTopTabIndicatorColor = getColor("selectedTopTabIndicatorColor", getDefaultSelectedTopTabIndicatorColor());

        // TODO: Uncomment once we support drawBelowTopBar again
        //result.drawScreenBelowTopBar = params.getBoolean("drawBelowTopBar", isDefaultScreenBelowTopBar());
        result.drawScreenBelowTopBar = true;

        result.bottomTabsHidden = getBoolean("bottomTabsHidden", getDefaultBottomTabsHidden());
        result.drawScreenAboveBottomTabs = !result.bottomTabsHidden &&
                                           params.getBoolean("drawScreenAboveBottomTabs", getDefaultDrawScreenAboveBottomTabs());
        result.bottomTabsHiddenOnScroll =
                getBoolean("bottomTabsHiddenOnScroll", getDefaultBottomTabsHiddenOnScroll());
        result.bottomTabsColor = getColor("bottomTabsColor", getDefaultBottomTabsColor());
        result.bottomTabsButtonColor = getColor("bottomTabsButtonColor", getDefaultBottomTabsButtonColor());
        result.selectedBottomTabsButtonColor =
                getColor("bottomTabsSelectedButtonColor", getDefaultSelectedBottomTabsButtonColor());
        result.bottomTabBadgeTextColor = getColor("bottomTabBadgeTextColor", getBottomTabBadgeTextColor());
        result.bottomTabBadgeBackgroundColor = getColor("bottomTabBadgeBackgroundColor", getBottomTabBadgeBackgroundColor());

        result.navigationBarColor = getColor("navigationBarColor", getDefaultNavigationColor());
        result.forceTitlesDisplay = getBoolean("forceTitlesDisplay", getDefaultForceTitlesDisplay());

        return result;
    }

    private boolean getDefaultDrawScreenAboveBottomTabs() {
        return AppStyle.appStyle == null || AppStyle.appStyle.drawScreenAboveBottomTabs;
    }

    private StyleParams.Color getDefaultSelectedTopTabIndicatorColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.selectedTopTabIndicatorColor;
    }

    private int getDefaultSelectedTopTabIndicatorHeight() {
        return AppStyle.appStyle == null ? -1 : AppStyle.appStyle.selectedTopTabIndicatorHeight;
    }

    private StyleParams.Color getDefaultSelectedTopTabTextColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.selectedTopTabTextColor;
    }

    private StyleParams.Color getDefaultNavigationColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.navigationBarColor;
    }

    private boolean getDefaultForceTitlesDisplay() {
        return AppStyle.appStyle != null && AppStyle.appStyle.forceTitlesDisplay;
    }

    private StyleParams.Color getDefaultSelectedBottomTabsButtonColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.selectedBottomTabsButtonColor;
    }

    private StyleParams.Color getBottomTabBadgeTextColor() {
        return new StyleParams.Color();
    }

    private StyleParams.Color getBottomTabBadgeBackgroundColor() {
        return new StyleParams.Color();
    }

    private StyleParams.Color getDefaultBottomTabsButtonColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.bottomTabsButtonColor;
    }

    private StyleParams.Color getDefaultBottomTabsColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.bottomTabsColor;
    }

    private boolean getDefaultBottomTabsHiddenOnScroll() {
        return AppStyle.appStyle != null && AppStyle.appStyle.bottomTabsHiddenOnScroll;
    }

    private boolean getDefaultBottomTabsHidden() {
        return AppStyle.appStyle != null && AppStyle.appStyle.bottomTabsHidden;
    }

    private boolean isDefaultScreenBelowTopBar() {
        return AppStyle.appStyle == null || AppStyle.appStyle.drawScreenBelowTopBar;
    }

    private boolean getDefaultTopTabsHidden() {
        return AppStyle.appStyle != null && AppStyle.appStyle.topTabsHidden;
    }

    private StyleParams.Color getDefaultTopTabTextColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.topTabTextColor;
    }

    private boolean getDefaultBackButtonHidden() {
        return AppStyle.appStyle != null && AppStyle.appStyle.backButtonHidden;
    }

    private StyleParams.Color getDefaultTitleBarColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.titleBarTitleColor;
    }

    private StyleParams.Color getDefaultSubtitleBarColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.titleBarSubtitleColor;
    }

    private StyleParams.Color getTitleBarButtonColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.titleBarButtonColor;
    }

    private StyleParams.Color getTitleBarDisabledButtonColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color(Color.LTGRAY) : AppStyle.appStyle.titleBarDisabledButtonColor;
    }

    private boolean getDefaultTopBarHidden() {
        return AppStyle.appStyle != null && AppStyle.appStyle.titleBarHidden;
    }

    private StyleParams.Color getDefaultTopBarColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.topBarColor;
    }

    private StyleParams.Color getDefaultStatusBarColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.statusBarColor;
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return params.containsKey(key) ? params.getBoolean(key) : defaultValue;
    }

    private StyleParams.Color getColor(String key, StyleParams.Color defaultColor) {
        StyleParams.Color color = StyleParams.Color.parse(params.getString(key));
        if (color.hasColor()) {
            return color;
        } else {
            return defaultColor != null && defaultColor.hasColor() ? defaultColor : color;
        }
    }

    private int getInt(String selectedTopTabIndicatorHeight, int defaultSelectedTopTabIndicatorHeight) {
        return params.containsKey(selectedTopTabIndicatorHeight) ?
                (int) params.getDouble(selectedTopTabIndicatorHeight) :
                defaultSelectedTopTabIndicatorHeight;
    }
}
