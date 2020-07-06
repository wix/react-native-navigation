package com.reactnativenavigation.views.toptabs;

import android.content.Context;

import com.reactnativenavigation.controllers.viewcontroller.ViewController;
import com.reactnativenavigation.controllers.toptabs.TopTabsAdapter;

import java.util.List;

public class TopTabsLayoutCreator {
    private Context context;
    private List<ViewController> tabs;

    public TopTabsLayoutCreator(Context context, List<ViewController> tabs) {
        this.context = context;
        this.tabs = tabs;
    }

    public TopTabsViewPager create() {
        return new TopTabsViewPager(context, tabs, new TopTabsAdapter(tabs));
    }
}
