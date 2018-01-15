package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

import java.util.List;

@SuppressLint("ViewConstructor")
public class TopTabsLayout extends RelativeLayout implements Component, TitleBarButton.OnClickListener {

    private TopBar topBar;
    private TopTabsViewPager viewPager;

    public TopTabsLayout(Context context, List<ViewController> tabs, TopTabsAdapter adapter) {
        super(context);
        viewPager = new TopTabsViewPager(context, tabs, adapter);
        topBar = new TopBar(context, this);
        topBar.setId(View.generateViewId());

        initViews();
    }

    private void initViews() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(BELOW, topBar.getId());
        addView(topBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(viewPager, layoutParams);
        topBar.setupTopTabsWithViewPager(viewPager);
    }

    @Override
    public void applyOptions(Options options) {

    }

    @Override
    public void drawBehindTopBar() {

    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {

    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public ViewPager getViewPager() {
        return viewPager;
    }

    public void switchToTab(int index) {
        viewPager.setCurrentItem(index);
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    @Override
    public void onPress(String buttonId) {
        viewPager.sendOnNavigationButtonPressed(buttonId);
    }
}
