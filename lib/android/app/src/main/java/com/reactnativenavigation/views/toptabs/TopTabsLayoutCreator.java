package com.reactnativenavigation.views.toptabs;

import android.content.Context;

import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;

import java.util.List;

public class TopTabsLayoutCreator {
    private final Context context;
    private final List<ViewController<?>> tabs;

    public TopTabsLayoutCreator(Context context, List<ViewController<?>> tabs) {
        this.context = context;
        this.tabs = tabs;
    }

    public TopTabsViewPager create() {
        return new TopTabsViewPager(context, tabs, new TopTabsAdapter(tabs));
    }
}
