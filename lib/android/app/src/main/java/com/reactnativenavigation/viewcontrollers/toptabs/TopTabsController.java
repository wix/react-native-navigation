package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTabsLayout;
import com.reactnativenavigation.views.TopTabsLayoutCreator;

import java.util.Collection;
import java.util.List;

public class TopTabsController extends ParentController implements NavigationOptionsListener {

    private List<TopTabController> tabs;
    private TopTabsLayout topTabsLayout;
    private TopTabsLayoutCreator viewCreator;
    private Options options;

    public TopTabsController(Activity activity, String id, List<TopTabController> tabs, TopTabsLayoutCreator viewCreator, Options options) {
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
        topTabsLayout = viewCreator.create();
        return topTabsLayout;
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        return tabs;
    }

    @Override
    public void onViewAppeared() {
        applyOptions(options);
        performOnCurrentTab(TopTabController::onViewAppeared);
    }

    @Override
    public void onViewDisappear() {
        performOnCurrentTab(TopTabController::onViewDisappear);
    }

    @Override
    public void applyOptions(Options options) {
        topTabsLayout.applyOptions(options);
    }

    @Override
    public void mergeOptions(Options options) {

    }

    public void switchToTab(int index) {
        topTabsLayout.switchToTab(index);
    }

    private void performOnCurrentTab(Task<TopTabController> task) {
        task.run(tabs.get(topTabsLayout.getCurrentItem()));
    }
}
