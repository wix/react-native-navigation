package com.reactnativenavigation.viewcontrollers.toptabs;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class TopTabsViewPager extends ViewPager implements ContainerViewController.IReactView {
    private static final int OFFSCREEN_PAGE_LIMIT = 99;
    private final List<ViewController> tabs;

    public TopTabsViewPager(Context context, List<ViewController> tabs) {
        super(context);
        this.tabs = tabs;
        init(tabs);
    }

    private void init(List<ViewController> tabs) {
        setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        for (ViewController tab : tabs) {
            addView(tab.getView(), new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        }
        setAdapter(new TopTabsAdapter(tabs));
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public View asView() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void sendContainerStart() {

    }

    @Override
    public void sendContainerStop() {

    }
}
