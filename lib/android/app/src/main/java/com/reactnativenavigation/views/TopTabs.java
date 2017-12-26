package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;

public class TopTabs extends TabLayout {
    private final TopTabsStyleHelper styleHelper;

    public TopTabs(Context context) {
        super(context);
        styleHelper = new TopTabsStyleHelper(this);
    }

    public void setFontFamily(int tabIndex, Typeface fontFamily) {
        styleHelper.setFontFamily(tabIndex, fontFamily);
    }
}
