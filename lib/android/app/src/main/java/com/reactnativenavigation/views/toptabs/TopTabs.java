package com.reactnativenavigation.views.toptabs;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.stack.topbar.TopBar;

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

    public void applyTopTabsColors(Colour selectedTabColor, Colour unselectedTabColor) {
        styleHelper.applyTopTabsColors(selectedTabColor, unselectedTabColor);
    }

    public void applyTopTabsFontSize(Number fontSize) {
        styleHelper.applyTopTabsFontSize(fontSize);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    public void setVisibility(TopBar topBar, boolean visible) {
        if (visible && getTabCount() > 0) {
            if(getVisibility() != VISIBLE){
                setVisibility(VISIBLE);
                topBar.addTabsHeightPx(getLayoutParams().height);
            }
        } else {
            setVisibility(GONE);
        }
    }

    public void clear() {
        setupWithViewPager(null);
        ViewUtils.removeFromParent(this);
    }

    public void init(ViewPager viewPager) {
        setupWithViewPager(viewPager);
    }
}
