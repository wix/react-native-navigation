package com.reactnativenavigation.params.parsers;

import android.graphics.Color;
import android.os.Bundle;

import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.Orientation;
import com.reactnativenavigation.params.StyleParams;

public class StyleParamsParser {
    private Bundle params;

    public StyleParamsParser(Bundle params) {
        this.params = params;
    }

    public StyleParams parse() {
        if (params == null) {
            return createDefaultStyleParams();
        }

        StyleParams result = new StyleParams();
        result.orientation = Orientation.fromString(params.getString("orientation", getDefaultOrientation()));
        result.statusBarColor = getColor("statusBarColor", getDefaultStatusBarColor());
        result.contextualMenuStatusBarColor = getColor("contextualMenuStatusBarColor", getDefaultContextualMenuStatusBarColor());
        result.contextualMenuButtonsColor = getColor("contextualMenuButtonsColor", getDefaultContextualMenuButtonsColor());
        result.contextualMenuBackgroundColor = getColor("contextualMenuBackgroundColor", getDefaultContextualMenuBackgroundColor());

        result.topBarColor = getColor("topBarColor", getDefaultTopBarColor());
        result.titleBarHideOnScroll = getBoolean("titleBarHideOnScroll", getDefaultTitleBarHideOnScroll());
        result.topBarTransparent = getBoolean("topBarTransparent", getDefaultTopBarHidden());
        result.topBarCollapseOnScroll = getBoolean("topBarCollapseOnScroll", false);
        result.drawScreenBelowTopBar = params.getBoolean("drawBelowTopBar", getDefaultScreenBelowTopBar());
        if (result.topBarTransparent) {
            result.drawScreenBelowTopBar = false;
        }
        result.collapsingTopBarParams = new CollapsingTopBarParamsParser(params, result.titleBarHideOnScroll, result.drawScreenBelowTopBar).parse();
        result.titleBarHidden = getBoolean("titleBarHidden", getDefaultTopBarHidden());
        result.topBarElevationShadowEnabled = getBoolean("topBarElevationShadowEnabled", getDefaultTopBarElevationShadowEnabled());
        result.titleBarTitleColor = getColor("titleBarTitleColor", getDefaultTitleBarColor());
        result.topBarTranslucent = getBoolean("topBarTranslucent", getDefaultTopBarTranslucent());

        result.titleBarSubtitleColor = getColor("titleBarSubtitleColor", getDefaultSubtitleBarColor());
        result.titleBarButtonColor = getColor("titleBarButtonColor", getTitleBarButtonColor());
        result.titleBarDisabledButtonColor = getColor("titleBarDisabledButtonColor", getTitleBarDisabledButtonColor());
        result.titleBarTitleFont = getFont("titleBarTitleFontFamily", getDefaultTitleTextFontFamily());
        result.titleBarTitleTextCentered = getBoolean("titleBarTitleTextCentered", false);
        result.backButtonHidden = getBoolean("backButtonHidden", getDefaultBackButtonHidden());
        result.topTabsHidden = getBoolean("topTabsHidden", getDefaultTopTabsHidden());

        result.topTabTextColor = getColor("topTabTextColor", getDefaultTopTabTextColor());
        result.topTabIconColor = getColor("topTabIconColor", getDefaultTopTabIconColor());
        result.selectedTopTabIconColor = getColor("selectedTopTabIconColor", getDefaultSelectedTopTabIconColor());
        result.selectedTopTabTextColor = getColor("selectedTopTabTextColor", getDefaultSelectedTopTabTextColor());
        result.selectedTopTabIndicatorHeight = getInt("selectedTopTabIndicatorHeight", getDefaultSelectedTopTabIndicatorHeight());
        result.selectedTopTabIndicatorColor = getColor("selectedTopTabIndicatorColor", getDefaultSelectedTopTabIndicatorColor());
        result.topTabsScrollable = getBoolean("topTabsScrollable", getDefaultTopTabsScrollable());

        result.screenBackgroundColor = getColor("screenBackgroundColor", getDefaultScreenBackgroundColor());

        result.bottomTabsHidden = getBoolean("bottomTabsHidden", getDefaultBottomTabsHidden());
        result.drawScreenAboveBottomTabs = !result.bottomTabsHidden &&
                                           params.getBoolean("drawScreenAboveBottomTabs", getDefaultDrawScreenAboveBottomTabs());
        if (result.titleBarHideOnScroll) {
            result.drawScreenAboveBottomTabs = false;
        }
        result.bottomTabsHiddenOnScroll = getBoolean("bottomTabsHiddenOnScroll", getDefaultBottomTabsHiddenOnScroll());
        result.bottomTabsColor = getColor("bottomTabsColor", getDefaultBottomTabsColor());
        result.bottomTabsButtonColor = getColor("bottomTabsButtonColor", getDefaultBottomTabsButtonColor());
        result.selectedBottomTabsButtonColor =
                getColor("bottomTabsSelectedButtonColor", getDefaultSelectedBottomTabsButtonColor());
        result.bottomTabBadgeTextColor = getColor("bottomTabBadgeTextColor", getBottomTabBadgeTextColor());
        result.bottomTabBadgeBackgroundColor = getColor("bottomTabBadgeBackgroundColor", getBottomTabBadgeBackgroundColor());

        result.navigationBarColor = getColor("navigationBarColor", getDefaultNavigationColor());
        result.forceTitlesDisplay = getBoolean("forceTitlesDisplay", getDefaultForceTitlesDisplay());

        result.bottomTabFontFamily = getFont("bottomTabFontFamily", getDefaultBottomTabsFontFamily());

        return result;
    }

    private String getDefaultOrientation() {
        return AppStyle.appStyle == null ? null : AppStyle.appStyle.orientation.name;
    }

    private StyleParams createDefaultStyleParams() {
        StyleParams result = new StyleParams();
        result.titleBarDisabledButtonColor = getTitleBarDisabledButtonColor();
        result.topBarElevationShadowEnabled = true;
        result.titleBarHideOnScroll = false;
        result.orientation = Orientation.auto;
        return result;
    }

    private StyleParams.Color getDefaultContextualMenuStatusBarColor() {
        return new StyleParams.Color(Color.parseColor("#7c7c7c"));
    }

    private StyleParams.Color getDefaultContextualMenuBackgroundColor() {
        return new StyleParams.Color(Color.WHITE);
    }

    private StyleParams.Color getDefaultContextualMenuButtonsColor() {
        return new StyleParams.Color(Color.parseColor("#757575"));
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

    private StyleParams.Color getDefaultSelectedTopTabIconColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.selectedTopTabIconColor;
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

    private boolean getDefaultScreenBelowTopBar() {
        return AppStyle.appStyle != null && AppStyle.appStyle.drawScreenBelowTopBar;
    }

    private StyleParams.Color getDefaultScreenBackgroundColor() {
        return AppStyle.appStyle != null ? AppStyle.appStyle.screenBackgroundColor : getColor("screenBackgroundColor", new StyleParams.Color());
    }

    private boolean getDefaultTopTabsHidden() {
        return AppStyle.appStyle != null && AppStyle.appStyle.topTabsHidden;
    }

    private StyleParams.Color getDefaultTopTabTextColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.topTabTextColor;
    }

    private boolean getDefaultTopTabsScrollable() {
        return AppStyle.appStyle != null && AppStyle.appStyle.topTabsScrollable;
    }

    private StyleParams.Color getDefaultTopTabIconColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.topTabIconColor;
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
        return AppStyle.appStyle != null && AppStyle.appStyle.topBarTransparent;
    }

    private boolean getDefaultTopBarElevationShadowEnabled() {
        return AppStyle.appStyle == null || AppStyle.appStyle.topBarElevationShadowEnabled;
    }

    private boolean getDefaultTopBarTranslucent() {
        return AppStyle.appStyle != null && AppStyle.appStyle.topBarTranslucent;
    }

    private boolean getDefaultTitleBarHideOnScroll() {
        return AppStyle.appStyle != null && AppStyle.appStyle.titleBarHideOnScroll;
    }

    private StyleParams.Color getDefaultTopBarColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.topBarColor;
    }

    private StyleParams.Color getDefaultStatusBarColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.statusBarColor;
    }

    private StyleParams.Font getDefaultBottomTabsFontFamily() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.bottomTabFontFamily;
    }

    private StyleParams.Font getDefaultTitleTextFontFamily() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.titleBarTitleFont;
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return params.containsKey(key) ? params.getBoolean(key) : defaultValue;
    }

    private StyleParams.Color getColor(String key, StyleParams.Color defaultColor) {
        StyleParams.Color color = StyleParams.Color.parse(params, key);
        if (color.hasColor()) {
            return color;
        } else {
            return defaultColor != null && defaultColor.hasColor() ? defaultColor : color;
        }
    }

    private StyleParams.Font getFont(String titleBarTitleFontFamily, StyleParams.Font defaultFont) {
        StyleParams.Font font = new StyleParams.Font(params.getString(titleBarTitleFontFamily));
        return font.hasFont() ? font : defaultFont;
    }

    private int getInt(String key, int defaultValue) {
        return params.containsKey(key) ? params.getInt(key) : defaultValue;
    }
}
