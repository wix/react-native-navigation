package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

import java.util.List;

@SuppressLint("ViewConstructor")
public class TopTabsLayout extends LinearLayout {

    private TopBar topBar;
    private TopTabsViewPager viewPager;

    public TopTabsLayout(Context context, List<ViewController> tabs) {
        super(context);
        topBar = new TopBar(context);
        viewPager = new TopTabsViewPager(context, tabs);
        initViews();
    }

    private void initViews() {
        setOrientation(VERTICAL);
        addView(topBar);
        addView(viewPager);
    }
}
