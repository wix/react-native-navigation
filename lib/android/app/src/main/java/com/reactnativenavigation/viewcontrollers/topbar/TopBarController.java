package com.reactnativenavigation.viewcontrollers.topbar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBar;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;


public class TopBarController {
    private TopBar topBar;

    public View createTopBar(Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewCreator backgroundViewCreator, TopBarButtonController.OnClickListener topBarButtonClickListener, StackLayout stackLayout) {
        if (topBar == null) {
            topBar = new TopBar(context, buttonCreator, titleBarReactViewCreator, backgroundViewCreator, topBarButtonClickListener, stackLayout);
            topBar.setId(CompatUtils.generateViewId());
        }
        return topBar;
    }

    public void clear() {
        topBar.clear();
    }

    public TopBar getTopBar() {
        return topBar;
    }

    public void initTopTabs(ViewPager viewPager) {
        topBar.initTopTabs(viewPager);
    }

    public void clearTopTabs() {
        topBar.clearTopTabs();
    }
}
