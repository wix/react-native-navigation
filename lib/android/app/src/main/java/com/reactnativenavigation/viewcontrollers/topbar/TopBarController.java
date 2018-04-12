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


public class TopBarController {
    private TopBar topBar;

    public View createView(Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener topBarButtonClickListener, StackLayout stackLayout) {
        if (topBar == null) {
            topBar = createTopBar(context, buttonCreator, titleBarReactViewCreator, topBarBackgroundViewController, topBarButtonClickListener, stackLayout);
            topBar.setId(CompatUtils.generateViewId());
        }
        return topBar;
    }

    protected TopBar createTopBar(Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener topBarButtonClickListener, StackLayout stackLayout) {
        return new TopBar(context, buttonCreator, titleBarReactViewCreator, topBarBackgroundViewController, topBarButtonClickListener, stackLayout);
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
