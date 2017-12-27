package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;

import com.reactnativenavigation.views.style.Color;

public class TopTabs extends TabLayout {
    private final TopTabsStyleHelper styleHelper;

    public TopTabs(Context context) {
        super(context);
        styleHelper = new TopTabsStyleHelper(this);
    }

    public void setFontFamily(int tabIndex, Typeface fontFamily) {
        styleHelper.setFontFamily(tabIndex, fontFamily);
    }

    public int[] getSelectedTabColors() {
        return SELECTED_STATE_SET;
    }

    public int[] getDefaultTabColors() {
        return EMPTY_STATE_SET;
    }

    public void applyTopTabsColors(Color selectedTabColor, Color unselectedTabColor) {
        styleHelper.applyTopTabsColors(selectedTabColor, unselectedTabColor);
    }
}
