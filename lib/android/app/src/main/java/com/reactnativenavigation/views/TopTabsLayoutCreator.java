package com.reactnativenavigation.views;

import android.content.Context;

import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;

import java.util.List;

public class TopTabsLayoutCreator {
    private Context context;
    private List<TopTabController> tabs;

    public TopTabsLayoutCreator(Context context, List<TopTabController> tabs) {
        this.context = context;
        this.tabs = tabs;
    }

    public TopTabsLayout create() {
        return new TopTabsLayout(context, tabs, new TopTabsAdapter(tabs));
    }
}
