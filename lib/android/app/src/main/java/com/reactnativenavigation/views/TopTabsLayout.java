package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

import java.util.List;

@SuppressLint("ViewConstructor")
public class TopTabsLayout extends LinearLayout implements Container {

    private TopBar topBar;
    private TopTabsViewPager viewPager;
    private final OptionsPresenter optionsPresenter;

    public TopTabsLayout(Context context, List<TopTabController> tabs, TopTabsAdapter adapter) {
        super(context);
        topBar = new TopBar(context);
        optionsPresenter = new OptionsPresenter(topBar, viewPager);
        viewPager = new TopTabsViewPager(context, tabs, adapter);
        initViews();
    }

    private void initViews() {
        setOrientation(VERTICAL);
        addView(topBar);
        addView(viewPager);
        topBar.setupTopTabsWithViewPager(viewPager);
    }

    @Override
    public void applyOptions(NavigationOptions options) {
        optionsPresenter.applyOptions(options);
    }

    @Override
    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public ViewPager getViewPager() {
        return viewPager;
    }

    public void switchToTab(int index) {
        viewPager.setCurrentItem(index);
    }
}
