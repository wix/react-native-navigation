package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTabsLayoutCreator;
import com.reactnativenavigation.views.TopTabsViewPager;

import java.util.Collection;
import java.util.List;

public class TopTabsController extends ParentController implements NavigationOptionsListener {

    private List<ViewController> tabs;
    private TopTabsViewPager topTabsViewPager;
    private TopTabsLayoutCreator viewCreator;
    private Options options;

    public TopTabsController(Activity activity, String id, List<ViewController> tabs, TopTabsLayoutCreator viewCreator, Options options) {
        super(activity, id);
        this.viewCreator = viewCreator;
        this.options = options;
        this.tabs = tabs;
        for (ViewController tab : tabs) {
            tab.setParentController(this);
        }
    }

    @NonNull
    @Override
    protected ViewGroup createView() {
        topTabsViewPager = viewCreator.create();
        return topTabsViewPager;
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        return tabs;
    }

    @Override
    public void onViewAppeared() {
        applyOptions(options);
        applyOnParentController(parentController -> parentController.setupTopTabsWithViewPager((ViewPager) getView()));
        performOnCurrentTab(ViewController::onViewAppeared);
    }

    @Override
    public void onViewDisappear() {
        performOnCurrentTab(ViewController::onViewDisappear);
    }

    @Override
    public void applyOptions(Options options) {
        topTabsViewPager.applyOptions(options);
    }

    @Override
    public void mergeOptions(Options options) {

    }

    public void switchToTab(int index) {
        topTabsViewPager.switchToTab(index);
    }

    private void performOnCurrentTab(Task<ViewController> task) {
        task.run(tabs.get(topTabsViewPager.getCurrentItem()));
    }
}
