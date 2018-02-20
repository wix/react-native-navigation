package com.reactnativenavigation.params.parsers;

import android.graphics.Color;
import android.os.Bundle;

import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.params.Orientation;
import com.reactnativenavigation.params.StatusBarTextColorScheme;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.utils.ViewUtils;

public class StyleParamsParser {
    private Bundle params;

    public StyleParamsParser(Bundle params) {
        this.params = params;
    }

    public StyleParamsParser merge(Bundle b) {
        params.putAll(b);
        return this;
    }

    public StyleParams parse() {
        if (params == null) {
            return createDefaultStyleParams();
        }

        StyleParams result = new StyleParams(params);
        result.orientation = Orientation.fromString(params.getString("orientation", getDefaultOrientation()));
        result.screenAnimationType = params.getString("screenAnimationType", getDefaultScreenAnimationType());
        result.statusBarColor = getColor("statusBarColor", getDefaultStatusBarColor());
        result.statusBarHidden = getBoolean("statusBarHidden", getDefaultStatusHidden());
        result.statusBarTextColorScheme = StatusBarTextColorScheme.fromString(params.getString("statusBarTextColorScheme"), getDefaultStatusBarTextColorScheme());
        result.drawUnderStatusBar = params.getBoolean("drawUnderStatusBar", getDefaultDrawUnderStatusBar());
        result.contextualMenuStatusBarColor = getColor("contextualMenuStatusBarColor", getDefaultContextualMenuStatusBarColor());
        result.contextualMenuButtonsColor = getColor("contextualMenuButtonsColor", getDefaultContextualMenuButtonsColor());
        result.contextualMenuBackgroundColor = getColor("contextualMenuBackgroundColor", getDefaultContextualMenuBackgroundColor());

        result.topBarColor = getColor("topBarColor", getDefaultTopBarColor());
        result.topBarReactView = params.getString("topBarReactView");
        result.topBarReactViewAlignment = params.getString("topBarReactViewAlignment");
        result.topBarReactViewInitialProps = getBundle("topBarReactViewInitialProps");
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
        result.topBarBorderColor = getColor("topBarBorderColor", getDefaultTopBarBorderColor());
        result.topBarBorderWidth = Float.parseFloat(params.getString("topBarBorderWidth", getDefaultTopBarBorderWidth()));

        result.titleBarSubtitleColor = getColor("titleBarSubtitleColor", getDefaultSubtitleBarColor());
        result.titleBarSubtitleFontSize = getInt("titleBarSubtitleFontSize", getDefaultSubtitleTextFontSize());
        result.titleBarSubtitleFontFamily = getFont("titleBarSubtitleFontFamily", getDefaultSubtitleFontFamily());
        result.titleBarButtonColor = getColor("titleBarButtonColor", getTitleBarButtonColor());
        result.titleBarButtonFontFamily = getFont("titleBarButtonFontFamily", getDefaultTitleBarButtonFont());
        result.titleBarDisabledButtonColor = getColor("titleBarDisabledButtonColor", getTitleBarDisabledButtonColor());
        result.titleBarTitleFont = getFont("titleBarTitleFontFamily", getDefaultTitleTextFontFamily());
        result.titleBarTitleFontSize = getInt("titleBarTitleFontSize", getDefaultTitleTextFontSize());
        result.titleBarTitleFontBold = getBoolean("titleBarTitleFontBold", getDefaultTitleTextFontBold());
        result.titleBarTitleTextCentered = getBoolean("titleBarTitleTextCentered", getDefaultTitleBarTextCentered());
        result.titleBarSubTitleTextCentered = getBoolean("titleBarSubTitleTextCentered", getDefaultTitleBarTextCentered());
        result.titleBarHeight = getInt("titleBarHeight", getDefaultTitleBarHeight());
        result.backButtonHidden = getBoolean("backButtonHidden", getDefaultBackButtonHidden());
        result.topTabsHidden = getBoolean("topTabsHidden", getDefaultTopTabsHidden());
        result.titleBarTopPadding = getInt("titleBarTopPadding", getTitleBarTopPadding());

        result.topTabTextColor = getColor("topTabTextColor", getDefaultTopTabTextColor());
        result.topTabTextFontFamily = getFont("topTabTextFontFamily", getDefaultTopTabTextFontFamily());
        result.topTabIconColor = getColor("topTabIconColor", getDefaultTopTabIconColor());
        result.selectedTopTabIconColor = getColor("selectedTopTabIconColor", getDefaultSelectedTopTabIconColor());
        result.selectedTopTabTextColor = getColor("selectedTopTabTextColor", getDefaultSelectedTopTabTextColor());
        result.selectedTopTabIndicatorHeight = getInt("selectedTopTabIndicatorHeight", getDefaultSelectedTopTabIndicatorHeight());
        result.selectedTopTabIndicatorColor = getColor("selectedTopTabIndicatorColor", getDefaultSelectedTopTabIndicatorColor());
        result.topTabsScrollable = getBoolean("topTabsScrollable", getDefaultTopTabsScrollable());
        result.topTabsHeight = getInt("topTabsHeight", getDefaultTopTabsHeight());

        result.screenBackgroundColor = getColor("screenBackgroundColor", getDefaultScreenBackgroundColor());

        result.bottomTabsInitialIndex = getInt("initialTabIndex", 0);
        result.bottomTabsHidden = getBoolean("bottomTabsHidden", getDefaultBottomTabsHidden());
        result.drawScreenAboveBottomTabs = !result.bottomTabsHidden &&
                                           params.getBoolean("drawScreenAboveBottomTabs", getDefaultDrawScreenAboveBottomTabs());
        if (result.titleBarHideOnScroll) {
            result.drawScreenAboveBottomTabs = false;
        }
        result.bottomTabsHiddenOnScroll = getBoolean("bottomTabsHiddenOnScroll", getDefaultBottomTabsHiddenOnScroll());
        result.bottomTabsColor = getColor("bottomTabsColor", getDefaultBottomTabsColor());
        result.bottomTabsButtonColor = getColor("bottomTabsButtonColor", getDefaultBottomTabsButtonColor());
        result.selectedBottomTabsButtonColor = getColor("bottomTabsSelectedButtonColor", getDefaultSelectedBottomTabsButtonColor());
        result.bottomTabBadgeTextColor = getColor("bottomTabBadgeTextColor", getBottomTabBadgeTextColor());
        result.bottomTabBadgeBackgroundColor = getColor("bottomTabBadgeBackgroundColor", getBottomTabBadgeBackgroundColor());

        result.navigationBarColor = getColor("navigationBarColor", getDefaultNavigationColor());
        result.forceTitlesDisplay = getBoolean("forceTitlesDisplay", getDefaultForceTitlesDisplay());

        result.bottomTabFontFamily = getFont("bottomTabFontFamily", getDefaultBottomTabsFontFamily());
        result.bottomTabFontSize = getIntegerOrNull("bottomTabFontSize");
        result.bottomTabSelectedFontSize = getIntegerOrNull("bottomTabSelectedFontSize");

        return result;
    }

    private String getDefaultScreenAnimationType() {
        return AppStyle.appStyle == null ? "slide-up" : AppStyle.appStyle.screenAnimationType;
    }

    private StatusBarTextColorScheme getDefaultStatusBarTextColorScheme() {
        return AppStyle.appStyle == null ? StatusBarTextColorScheme.Undefined : AppStyle.appStyle.statusBarTextColorScheme;
    }

    private String getDefaultOrientation() {
        return AppStyle.appStyle == null ? null : AppStyle.appStyle.orientation.name;
    }

    private StyleParams createDefaultStyleParams() {
        StyleParams result = new StyleParams(Bundle.EMPTY);
        result.titleBarDisabledButtonColor = getTitleBarDisabledButtonColor();
        result.topBarElevationShadowEnabled = true;
        result.titleBarHideOnScroll = false;
        result.orientation = Orientation.auto;
        result.bottomTabFontFamily = new StyleParams.Font();
        result.bottomTabFontSize = 10;
        result.bottomTabSelectedFontSize = 10;
        result.titleBarTitleFont = new StyleParams.Font();
        result.titleBarSubtitleFontFamily = new StyleParams.Font();
        result.titleBarButtonFontFamily = new StyleParams.Font();
        result.topTabTextFontFamily = new StyleParams.Font();
        result.titleBarHeight = -1;
        result.screenAnimationType = "slide-up";
        result.drawUnderStatusBar = false;
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

    private int getDefaultTopTabsHeight() {
        return AppStyle.appStyle == null ? -1 : AppStyle.appStyle.topTabsHeight;
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

    private StyleParams.Color getDefaultTopBarBorderColor() {
        return AppStyle.appStyle == null ? new StyleParams.Color() : AppStyle.appStyle.topBarBorderColor;
    }

    private String getDefaultTopBarBorderWidth() {
        return String.valueOf(AppStyle.appStyle == null ? ViewUtils.convertDpToPixel(1) : AppStyle.appStyle.topBarBorderWidth);
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

    private boolean getDefaultStatusHidden() {
        return AppStyle.appStyle != null && AppStyle.appStyle.statusBarHidden;
    }

    private boolean getDefaultDrawUnderStatusBar() {
        return AppStyle.appStyle != null && AppStyle.appStyle.drawUnderStatusBar;
    }

    private StyleParams.Font getDefaultBottomTabsFontFamily() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.bottomTabFontFamily;
    }

    private StyleParams.Font getDefaultTitleTextFontFamily() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.titleBarTitleFont;
    }

    private int getDefaultTitleTextFontSize() {
        return AppStyle.appStyle == null ? -1 : AppStyle.appStyle.titleBarTitleFontSize;
    }

    private int getDefaultSubtitleTextFontSize() {
        return AppStyle.appStyle == null ? -1 : AppStyle.appStyle.titleBarSubtitleFontSize;
    }

    private StyleParams.Font getDefaultSubtitleFontFamily() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.titleBarSubtitleFontFamily;
    }

    private StyleParams.Font getDefaultTopTabTextFontFamily() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.topTabTextFontFamily;
    }

    private StyleParams.Font getDefaultTitleBarButtonFont() {
        return AppStyle.appStyle == null ? new StyleParams.Font() : AppStyle.appStyle.titleBarButtonFontFamily;
    }

    private boolean getDefaultTitleTextFontBold() {
        return AppStyle.appStyle != null && AppStyle.appStyle.titleBarTitleFontBold;
    }

    private boolean getDefaultTitleBarTextCentered() {
        return AppStyle.appStyle != null && AppStyle.appStyle.titleBarTitleTextCentered;
    }

    private boolean getDefaultSubTitleBarTextCentered() {
        return AppStyle.appStyle != null && AppStyle.appStyle.titleBarSubTitleTextCentered;
    }

    private int getDefaultTitleBarHeight() {
        return AppStyle.appStyle == null ? -1 : AppStyle.appStyle.titleBarHeight;
    }

    private int getTitleBarTopPadding() {
        return AppStyle.appStyle == null ? 0 : AppStyle.appStyle.titleBarTopPadding;
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

    private Integer getIntegerOrNull(String key) {
        return params.containsKey(key) ? params.getInt(key) : null;
    }

    private Bundle getBundle(String key) {
        return params.containsKey(key) ? params.getBundle(key) : Bundle.EMPTY;
    }
}
