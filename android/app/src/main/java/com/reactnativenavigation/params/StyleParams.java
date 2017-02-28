package com.reactnativenavigation.params;

import android.os.Bundle;
import android.support.annotation.ColorInt;

public class StyleParams {
    public static class Color {
        @ColorInt
        private Integer color = null;

        public Color() {
            color = null;
        }

        public Color(Integer color) {
            this.color = color;
        }

        public boolean hasColor() {
            return color != null;
        }

        @ColorInt
        public int getColor() {
            if (!hasColor()) {
                throw new RuntimeException("Color undefined");
            }
            return color;
        }

        public static Color parse(Bundle bundle, String key) {
            return bundle.containsKey(key) ? new Color(bundle.getInt(key)) : new Color();
        }

        public String getHexColor() {
            return String.format("#%06X", (0xFFFFFF & getColor()));
        }

        public int getColor(int defaultColor) {
            return hasColor() ? getColor() : defaultColor;
        }
    }

    public Color statusBarColor;
    public Color contextualMenuStatusBarColor;
    public Color contextualMenuButtonsColor;
    public Color contextualMenuBackgroundColor;

    public Color topBarColor;
    public CollapsingTopBarParams collapsingTopBarParams;
    public boolean topBarCollapseOnScroll;
    public boolean topBarHidden;
    public boolean topBarElevationShadowEnabled;
    public boolean topTabsHidden;
    public boolean drawScreenBelowTopBar;

    public boolean titleBarHidden;
    public boolean titleBarHideOnScroll;
    public boolean topBarTransparent;
    public boolean topBarTranslucent;
    public Color titleBarTitleColor;
    public Color titleBarSubtitleColor;
    public Color titleBarButtonColor;
    public Color titleBarDisabledButtonColor;
    public boolean backButtonHidden;

    public Color topTabTextColor;
    public Color topTabIconColor;
    public Color selectedTopTabTextColor;
    public Color selectedTopTabIconColor;
    public int selectedTopTabIndicatorHeight;
    public Color selectedTopTabIndicatorColor;
    public boolean topTabScrollable;

    public Color screenBackgroundColor;

    public boolean drawScreenAboveBottomTabs;

    public Color snackbarButtonColor;

    public boolean bottomTabsHidden;
    public boolean bottomTabsHiddenOnScroll;
    public Color bottomTabsColor;
    public Color selectedBottomTabsButtonColor;
    public Color bottomTabsButtonColor;
    public boolean forceTitlesDisplay;
    public Color bottomTabBadgeTextColor;
    public Color bottomTabBadgeBackgroundColor;

    public Color navigationBarColor;
}
