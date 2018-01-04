package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.NavigationOptions;
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
    private NavigationOptions navigationOptions;

    public TopTabsController(Activity activity, String id, List<TopTabController> tabs, TopTabsLayoutCreator viewCreator, NavigationOptions navigationOptions) {
        super(activity, id);
        this.viewCreator = viewCreator;
        this.navigationOptions = navigationOptions;
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
        applyOptions(navigationOptions);
        performOnCurrentTab(TopTabController::onViewAppeared);
    }

    @Override
    public void onViewDisappear() {
        performOnCurrentTab(TopTabController::onViewDisappear);
    }

    @Override
    public void applyOptions(NavigationOptions options) {
        topTabsLayout.applyOptions(options);
    }

    @Override
    public void mergeNavigationOptions(NavigationOptions options) {

    }

    public void switchToTab(int index) {
        topTabsLayout.switchToTab(index);
    }

    private void performOnCurrentTab(Task<TopTabController> task) {
        task.run(tabs.get(topTabsLayout.getCurrentItem()));
    }
}
