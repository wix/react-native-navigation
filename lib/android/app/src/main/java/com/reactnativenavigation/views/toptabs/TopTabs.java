package com.reactnativenavigation.views.toptabs;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewManager;

import com.reactnativenavigation.anim.TopTabsCollapseBehavior;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.views.topbar.TopBar;

public class TopTabs extends TabLayout implements ScrollEventListener.ScrollAwareView {
    private final TopTabsStyleHelper styleHelper;
    private TopTabsCollapseBehavior collapseBehavior;

    public TopTabs(Context context, View parent) {
        super(context);
        styleHelper = new TopTabsStyleHelper(this);
        collapseBehavior = new TopTabsCollapseBehavior(this, parent);
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

    public void applyTopTabsFontSize(Number fontSize) {
        styleHelper.applyTopTabsFontSize(fontSize);
    }

    public void setVisibility(TopBar topBar, boolean visible) {
        if (visible && getTabCount() > 0) {
            if (getParent() == null) {
                topBar.addView(this, 1);
            }
            setVisibility(VISIBLE);
        } else {
            topBar.removeView(this);
        }
    }

    public void clear(ViewManager parent) {
        setupWithViewPager(null);
        parent.removeView(this);
    }

    public void init(ViewPager viewPager) {
        setupWithViewPager(viewPager);
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        collapseBehavior.enableCollapse(scrollEventListener);
    }

    public void disableCollapse() {
        collapseBehavior.disableCollapse();
    }
}
