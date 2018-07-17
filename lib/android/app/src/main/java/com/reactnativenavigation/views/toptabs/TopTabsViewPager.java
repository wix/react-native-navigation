package com.reactnativenavigation.views.toptabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.BELOW;

@SuppressLint("ViewConstructor")
public class TopTabsViewPager extends ViewPager implements Component, TopBarButtonController.OnClickListener {

    private static final int OFFSCREEN_PAGE_LIMIT = 99;
    private List<ViewController> tabs;

    public TopTabsViewPager(Context context, List<ViewController> tabs, TopTabsAdapter adapter) {
        super(context);
        this.tabs = tabs;
        initTabs(adapter);
    }

    private void initTabs(TopTabsAdapter adapter) {
        setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        for (ViewController tab : tabs) {
            addView(tab.getView(), new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        }
        setAdapter(adapter);
        addOnPageChangeListener(adapter);
    }

    @Override
    public void drawBehindTopBar() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.removeRule(BELOW);
        setLayoutParams(layoutParams);
    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.addRule(BELOW, topBar.getId());
        setLayoutParams(layoutParams);
    }

    @Override
    public boolean isRendered() {
        return tabs.size() != 0 && areAllTabsRendered();
    }

    private boolean areAllTabsRendered() {
        for (ViewController tab : tabs) {
            if (!tab.isRendered()) return false;
        }
        return true;
    }

    public void switchToTab(int index) {
        setCurrentItem(index);
    }

    @Override
    public void onPress(String buttonId) {
        ((IReactView) tabs.get(getCurrentItem()).getView()).sendOnNavigationButtonPressed(buttonId);
    }

    public void destroy() {
        for (ViewController tab : tabs) {
            tab.destroy();
        }
    }

    public boolean isCurrentView(View view) {
        for (ViewController tab : tabs) {
            if (tab.getView() == view) {
                return true;
            }
        }
        return false;
    }

    public void applyOptions(Options options) {

    }
}
