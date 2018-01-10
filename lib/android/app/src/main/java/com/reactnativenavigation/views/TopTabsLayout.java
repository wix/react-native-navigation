package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

import java.util.List;

@SuppressLint("ViewConstructor")
public class TopTabsLayout extends RelativeLayout implements Container {

    private TopBar topBar;
    private List<TopTabController> tabs;
    private TopTabsViewPager viewPager;
    private final OptionsPresenter optionsPresenter;

    public TopTabsLayout(Context context, List<TopTabController> tabs, TopTabsAdapter adapter) {
        super(context);
        this.tabs = tabs;
        topBar = new TopBar(context, this);
        topBar.setId(View.generateViewId());
        viewPager = new TopTabsViewPager(context, tabs, adapter);
        optionsPresenter = new OptionsPresenter(this);
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
    public void applyOptions(NavigationOptions options) {
        optionsPresenter.applyOptions(options);
    }

    @Override
    public void sendOnNavigationButtonPressed(String id) {
        viewPager.sendOnNavigationButtonPressed(id);
    }

    @Override
    public TopBar getTopBar() {
        return topBar;
    }

    @Override
    public View getContentView() {
        return viewPager;
    }

    @Override
    public void drawBehindTopBar() {

    }

    @Override
    public void drawBelowTopBar() {

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

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }
}
