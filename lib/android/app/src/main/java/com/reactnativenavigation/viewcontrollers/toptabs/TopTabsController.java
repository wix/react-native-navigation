package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.ViewVisibilityListenerAdapter;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.TopTabsLayoutCreator;
import com.reactnativenavigation.views.TopTabsViewPager;

import java.util.Collection;
import java.util.List;

public class TopTabsController extends ParentController<TopTabsViewPager> implements NavigationOptionsListener {

    private List<ViewController> tabs;
    private TopTabsLayoutCreator viewCreator;

    public TopTabsController(Activity activity, String id, List<ViewController> tabs, TopTabsLayoutCreator viewCreator, Options options) {
        super(activity, id, options);
        this.viewCreator = viewCreator;
        this.tabs = tabs;
        for (ViewController tab : tabs) {
            tab.setParentController(this);
            tab.setViewVisibilityListener(new ViewVisibilityListenerAdapter() {
                @Override
                public boolean onViewAppeared(View view) {
                    return getView().isCurrentView(view);
                }
            });
        }
    }

    @NonNull
    @Override
    protected TopTabsViewPager createView() {
        view = viewCreator.create();
        return (TopTabsViewPager) view;
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        return tabs;
    }

    @Override
    public void onViewAppeared() {
        applyOptions(options);
        applyOnParentController(parentController -> ((ParentController) parentController).setupTopTabsWithViewPager(getView()));
        performOnCurrentTab(ViewController::onViewAppeared);
    }

    @Override
    public void onViewWillDisappear() {
        super.onViewWillDisappear();
    }

    @Override
    public void onViewDisappear() {
        performOnCurrentTab(ViewController::onViewDisappear);
        applyOnParentController(parentController -> ((ParentController) parentController).clearTopTabs());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        performOnCurrentTab(tab -> tab.sendOnNavigationButtonPressed(buttonId));
    }

    @Override
    public void applyOptions(Options options) {
        getView().applyOptions(options);
    }

    @Override
    public void applyOptions(Options options, Component childComponent) {
        super.applyOptions(options, childComponent);
        applyOnParentController(parentController -> {
                Options opt = this.options.copy();
                ((ParentController) parentController).applyOptions(opt.clearTopTabOptions().clearTopTabsOptions(), childComponent);
            }
        );
    }

    @Override
    public void mergeOptions(Options options) {

    }

    public void switchToTab(int index) {
        getView().switchToTab(index);
    }

    private void performOnCurrentTab(Task<ViewController> task) {
        task.run(tabs.get(getView().getCurrentItem()));
    }
}
