package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.ViewVisibilityListenerAdapter;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.toptabs.TopTabsLayoutCreator;
import com.reactnativenavigation.views.toptabs.TopTabsViewPager;

import java.util.Collection;
import java.util.List;

public class TopTabsController extends ParentController<TopTabsViewPager> {

    private List<ViewController> tabs;
    private TopTabsLayoutCreator viewCreator;

    public TopTabsController(Activity activity, ChildControllersRegistry childRegistry, String id, List<ViewController> tabs, TopTabsLayoutCreator viewCreator, Options options, OptionsPresenter presenter) {
        super(activity, childRegistry, id, presenter, options);
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

    @Override
    protected ViewController getCurrentChild() {
        return tabs.get(getView().getCurrentItem());
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
        super.onViewAppeared();
        performOnParentController(parentController -> ((ParentController) parentController).setupTopTabsWithViewPager(getView()));
        performOnCurrentTab(ViewController::onViewAppeared);
    }

    @Override
    public void onViewWillDisappear() {
        super.onViewWillDisappear();
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        performOnCurrentTab(ViewController::onViewDisappear);
        performOnParentController(parentController -> ((ParentController) parentController).clearTopTabs());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        performOnCurrentTab(tab -> tab.sendOnNavigationButtonPressed(buttonId));
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        getView().applyOptions(options);
    }

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        performOnParentController(parentController -> ((ParentController) parentController).applyChildOptions(this.options.copy(), child));
    }

    @CallSuper
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        performOnParentController(parentController -> ((ParentController) parentController).applyChildOptions(options.copy(), child));
    }

    public void switchToTab(int index) {
        getView().switchToTab(index);
    }

    private void performOnCurrentTab(Task<ViewController> task) {
        task.run(tabs.get(getView().getCurrentItem()));
    }
}
