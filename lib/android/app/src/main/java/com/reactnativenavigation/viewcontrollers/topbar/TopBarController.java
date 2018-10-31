package com.reactnativenavigation.viewcontrollers.topbar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;


public class TopBarController {
    private TopBar topBar;

    public View createView(Context context, TopBarBackgroundViewController topBarBackgroundViewController, StackLayout stackLayout) {
        if (topBar == null) {
            topBar = createTopBar(context, topBarBackgroundViewController, stackLayout);
            topBar.setId(CompatUtils.generateViewId());
        }
        return topBar;
    }

    protected TopBar createTopBar(Context context, TopBarBackgroundViewController topBarBackgroundViewController, StackLayout stackLayout) {
        return new TopBar(context, topBarBackgroundViewController, stackLayout);
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
