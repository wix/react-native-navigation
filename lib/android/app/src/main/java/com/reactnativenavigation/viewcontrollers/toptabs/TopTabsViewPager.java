package com.reactnativenavigation.viewcontrollers.toptabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("ViewConstructor")
public class TopTabsViewPager extends ViewPager {
    private static final int OFFSCREEN_PAGE_LIMIT = 99;
    private List<ViewController> tabs;

    public TopTabsViewPager(Context context, List<ViewController> tabs, TopTabsAdapter adapter) {
        super(context);
        this.tabs = tabs;
        init(adapter);
    }

    private void init(TopTabsAdapter adapter) {
        setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        for (ViewController tab : tabs) {
            addView(tab.getView(), new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        }
        setAdapter(adapter);
        addOnPageChangeListener(adapter);
    }

    public void destroy() {
        for (ViewController tab : tabs) {
            tab.destroy();
        }
    }

    public void sendOnNavigationButtonPressed(String id) {
        ((ComponentViewController.IReactView) tabs.get(getCurrentItem()).getView()).sendOnNavigationButtonPressed(id);
    }
}
