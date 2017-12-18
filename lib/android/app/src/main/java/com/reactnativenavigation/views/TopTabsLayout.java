package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

import java.util.List;

@SuppressLint("ViewConstructor")
public class TopTabsLayout extends LinearLayout implements Container {

    private TopBar topBar;
    private List<TopTabController> tabs;
    private TopTabsViewPager viewPager;
    private final OptionsPresenter optionsPresenter;

    public TopTabsLayout(Context context, List<TopTabController> tabs) {
        super(context);
        topBar = new TopBar(context);
        this.tabs = tabs;
        viewPager = new TopTabsViewPager(context, tabs);
        optionsPresenter = new OptionsPresenter(topBar, this);
        initViews();
    }

    private void initViews() {
        setOrientation(VERTICAL);
        addView(topBar);
        addView(viewPager);
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


    public void performOnCurrentTab(Task<TopTabController> task) {
        task.run(tabs.get(viewPager.getCurrentItem()));
    }

    public void switchToTab(int index) {
        viewPager.setCurrentItem(index);
    }
}
