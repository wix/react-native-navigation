package com.reactnativenavigation.views.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.views.TopTabs;

public class TopTabsIconColorHelper {
    private final TopTabs topTabs;
    private final StyleParams styleParams;

    public TopTabsIconColorHelper(TopTabs topTabs, StyleParams styleParams) {
        this.topTabs = topTabs;
        this.styleParams = styleParams;
    }

    public void colorIcons(int selectedIconColor, int unselectedIconColor) {
        ColorStateList colorStateList = getIconColorStateList(styleParams, selectedIconColor, unselectedIconColor);
        colorIcons(colorStateList);
    }

    private ColorStateList getIconColorStateList(StyleParams style, int selectedIconColor, int unselectedIconColor) {
        int selectedColor = selectedIconColor;
        int unselectedColor = unselectedIconColor;
        if (style.topTabIconColor.hasColor()) {
            unselectedColor = style.topTabIconColor.getColor();
        }
        if (style.selectedTopTabIconColor.hasColor()) {
            selectedColor = style.selectedTopTabIconColor.getColor();
        }
        return createColorStateList(selectedColor, unselectedColor);
    }

    private ColorStateList createColorStateList(int selectedColor, int unselectedIconColor) {
        int[][] states = new int[][] {
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_enabled},
                new int[]{android.R.attr.state_focused, android.R.attr.state_pressed},
                new int[]{-android.R.attr.state_enabled},
                new int[]{}
        };
        int[] colors = new int[]{
                selectedColor,
                selectedColor,
                unselectedIconColor,
                unselectedIconColor,
                unselectedIconColor,
                unselectedIconColor
        };
        return new ColorStateList(states, colors);
    }

    private void colorIcons(ColorStateList colorStateList) {
        for (int i = 0; i < topTabs.getTabCount(); i++) {
            Drawable icon = topTabs.getTabAt(i).getIcon();
            if (icon != null) {
                icon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(icon, colorStateList);
            }
        }
    }
}
