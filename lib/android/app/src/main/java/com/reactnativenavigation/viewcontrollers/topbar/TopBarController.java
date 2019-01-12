package com.reactnativenavigation.viewcontrollers.topbar;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;


public class TopBarController {
    private static final int INITIAL_ELEVATION = 0;
    private TopBar topBar;

    public View createView(Context context, StackLayout stackLayout) {
        if (topBar == null) {
            topBar = createTopBar(context, stackLayout);
            topBar.setId(CompatUtils.generateViewId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) topBar.setElevation(INITIAL_ELEVATION);
        }
        return topBar;
    }

    protected TopBar createTopBar(Context context, StackLayout stackLayout) {
        return new TopBar(context, stackLayout);
    }

    public void clear() {
        topBar.clear();
    }

    public TopBar getView() {
        return topBar;
    }

    public void initTopTabs(ViewPager viewPager) {
        topBar.initTopTabs(viewPager);
    }

    public void clearTopTabs() {
        topBar.clearTopTabs();
    }
}
